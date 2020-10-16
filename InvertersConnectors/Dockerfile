FROM python:3.9.0b4-alpine3.12
COPY MeasurementsExtractor /bin/MeasurementsExtractor
COPY root /var/spool/cron/crontabs/root
COPY requirements.txt .
RUN apk update
RUN apk add --update gcc
RUN pip install --upgrade pip
RUN pip install -r requirements.txt
RUN chmod +x /bin/MeasurementsExtractor
CMD crond -l 2 -f