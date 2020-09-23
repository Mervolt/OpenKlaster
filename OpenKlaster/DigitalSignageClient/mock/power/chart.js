function promilOfWidth(promil) {
	return window.innerWidth * (promil / 1000);
}

function promilOfHeight(promil) {
	return window.innerHeight * (promil / 1000);
}

const margin = {
	top: promilOfHeight(10), right: promilOfWidth(300),
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
	let parser = d3.timeFormat('%Y-%m-%d_%H:%M:%S')
	return parser(date);
}

function main(config, data) {
	(function () {
		createChart(config, data);
		setTimeout(arguments.callee, 60000);
	})();
}

async function createChart(config, data) {
	const rootUrl = config['api']['rootUrl'];
	const powerProductionUrl = rootUrl + config['api']['powerProduction'];
	const powerConsumptionUrl = rootUrl + config['api']['powerConsumption'];
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

	draw(getMockData(), fillColor, strokeColor, powerProductionTitle)
}
function getMockData() {
	let data = []
	let parseDate = d3.timeParse('%Y-%m-%dT%H:%M:%S.%L%Z')
	data.push({timestamp: parseDate('2020-06-01T17:25:34.399Z'), value: 12.5})
	data.push({timestamp: parseDate('2020-06-01T17:36:34.399Z'), value: 156.5})
	data.push({timestamp: parseDate('2020-06-01T17:42:34.399Z'), value: 32.5})
	data.push({timestamp: parseDate('2020-06-01T17:48:34.399Z'), value: 66.5})
	data.push({timestamp: parseDate('2020-06-01T16:52:34.399Z'), value: 200.5})
	data.push({timestamp: parseDate('2020-06-01T15:52:34.399Z'), value: 270.5})
	data.push({timestamp: parseDate('2020-06-01T14:52:34.399Z'), value: 300.5})
	data.push({timestamp: parseDate('2020-06-01T13:52:34.399Z'), value: 350.5})
	data.push({timestamp: parseDate('2020-06-01T12:52:34.399Z'), value: 370.5})
	data.push({timestamp: parseDate('2020-06-01T11:52:34.399Z'), value: 400.5})
	data.push({timestamp: parseDate('2020-06-01T10:52:34.399Z'), value: 360.5})
	data.push({timestamp: parseDate('2020-06-01T09:52:34.399Z'), value: 310.5})
	data.push({timestamp: parseDate('2020-06-01T08:52:34.399Z'), value: 250.5})
	data.push({timestamp: parseDate('2020-06-01T07:52:34.399Z'), value: 180.5})
	data.push({timestamp: parseDate('2020-06-01T06:52:34.399Z'), value: 140.5})
	data.push({timestamp: parseDate('2020-06-01T05:52:34.399Z'), value: 120.5})
	data.sort((a, b) => {
		return a.timestamp < b.timestamp ? -1 : 1
	})
	return data
}
