
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
nohup /data/logstash/bin/logstash -f $DIR/logstash-elf.conf  -l /log/logstash/elf-kafka/ --path.data=$DIR/data --http.port 9610 --config.reload.automatic &
#echo $$ > elf-kafka.pid
