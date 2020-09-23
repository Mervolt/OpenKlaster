function promilOfWidth(promil) {
    return window.innerWidth * (promil / 1000);
}

function promilOfHeight(promil) {
    return window.innerHeight * (promil / 1000);
}

const margin = {
    top: promilOfHeight(10), right: promilOfWidth(50),
    bottom: promilOfHeight(50), left: promilOfWidth(0)
};
const width = window.innerWidth - margin.left - margin.right;
const height = window.innerHeight - margin.top - margin.bottom;

const powerConsumptionTitle = config['textTitles']['powerConsumptionTitle']
const powerProductionTitle = config['textTitles']['powerProductionTitle']

const svg = d3
    .select('#chart')
    .append('svg')
    .attr('width', width + margin.left + margin.right)
    .attr('height', height + margin.top + margin.bottom)

function draw(data, fillColor, strokeColor, chartTitle, strokeWidth = '1.5') {
    console.log(data)
    const xScale = d3.scaleTime()
        .domain([earliestDate(data), latestDate(data)])
        .range([0, width]);

    const yScale = d3
        .scaleLinear()
        .domain([0, maxValue(data)])
        .range([height, 0]);

    svg.append('g')
        .attr('class', 'axis')
        .attr('id', 'xAxis')
        .attr('transform', `translate(0, ${height})`)
        .call(d3.axisBottom(xScale));

    svg.append('g')
        .attr('class', 'axis')
        .attr('id', 'yAxis')
        .attr('transform', `translate(${width}, 0)`)
        .call(d3.axisRight(yScale));

    const line = d3.area()
        .x(d => {
            return xScale(d['timestamp']);
        })
        .y0(yScale(0))
        .y1(d => {
            return yScale(d['value']);
        });

    svg.append('path')
        .datum(data)
        .attr('id', 'priceChart')
        .attr("fill", fillColor)
        .attr("stroke", strokeColor)
        .attr('stroke-width', strokeWidth)
        .attr('d', line);
}

function maxValue(data) {
    return d3.max(data, d => {
        return d['value']
    })
}

function latestDate(data) {
    return d3.max(data, d => {
        return d['timestamp']
    })
}

function earliestDate(data) {
    return d3.min(data, d => {
        return d['timestamp']
    })
}

function parseDateToString(date) {
    let parser = d3.timeFormat('%Y-%m-%d %H:%M:%S')
    return parser(date);
}

function main(config) {
    (function () {
        createChart(config);
        setTimeout(arguments.callee, 60000);
    })();
}

async function createChart(config) {
    const powerProductionUrl = config['powerProduction'];
    const powerConsumptionUrl = config['powerConsumption'];
    const installationId = config['installationId'];
    const apiToken = config['apiToken'];

    const now = new Date()
    const h = 7

    now.setTime(now.getTime() + (h * 60 * 60 * 1000))
    const prevDay = new Date();

    svg.selectAll("*").remove();

    prevDay.setDate(now.getDate() - 1);

    const fillColor = config['colours']['fillColor']
    const strokeColor = config['colours']['strokeColor']

    drawAsync(powerProductionUrl, installationId, now, prevDay, apiToken, fillColor, strokeColor, powerProductionTitle)
}

async function drawAsync(url, id, now, prevDay, apiToken, fillColor, strokeColor, title) {
    console.log(url)
    const sendUrl = new URL(url);
    const params = {
        installationId: id,

        apiToken: apiToken
    };
    console.log(params)
    Object.keys(params).forEach(key => sendUrl.searchParams.append(key, params[key]))

    fetch(sendUrl, {
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(response => response.json())
        .then(data => {
            let output = []
            for (const measurement of data) {
                let parseDate = d3.timeParse('%Y-%m-%d %H:%M:%S')
                output.push({timestamp: parseDate(measurement.timestamp), value: measurement.value})
            }

            output.sort((a, b) => {
                return a.timestamp < b.timestamp ? -1 : 1
            })
            draw(output, fillColor, strokeColor, title)
        })
}

main(config)
