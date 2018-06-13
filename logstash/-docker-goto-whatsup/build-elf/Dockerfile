FROM docker.elastic.co/logstash/logstash:6.2.4
RUN rm -f /usr/share/logstash/pipeline/logstash.conf
ADD logstash-elf.conf /usr/share/logstash/pipeline/logstash-elf.conf
EXPOSE 9600