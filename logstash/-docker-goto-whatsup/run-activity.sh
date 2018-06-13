
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
nohup /data/logstash/bin/logstash -f $DIR/logstash-elf-activity.conf  -l /log/logstash/elf-activity-kafka/ --path.data=$DIR/data-activity --http.port 9611 --config.reload.automatic &
#echo $$ > elf-kafka.pid
