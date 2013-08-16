package storm.helloworld.spout;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import storm.helloworld.spout.template.AbstractSpout;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordReader extends AbstractSpout {
	
	private static final long serialVersionUID = 8629574520645820588L;	
	private SpoutOutputCollector collector;
	private FileReader fileReader;
	private boolean completed = false;
	private Logger logger = LoggerFactory.getLogger(WordReader.class);

	public void ack(Object msgId) {
		logger.info("OK:" + msgId);
	}

	public void fail(Object msgId) {
		logger.info("FAIL:" + msgId);
	}

	public void nextTuple() {

		if (completed) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// Do nothing
			}
			return;
		}
		
		String str;
		BufferedReader reader = new BufferedReader(fileReader);
		try {

			while ((str = reader.readLine()) != null) {
				this.collector.emit(new Values(str), str);
			}
		} catch (Exception e) {
			logger.error("Error reading tuple", e);
			throw new RuntimeException("Error reading tuple", e);
		} finally {
			completed = true;
		}
	}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		try {
			this.fileReader = new FileReader(conf.get("wordsFile").toString());
		} catch (FileNotFoundException e) {
			logger.error("Error reading file["+ conf.get("wordFile") + "]");
			throw new RuntimeException("Error reading file["+ conf.get("wordFile") + "]");
		}
		this.collector = collector;
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}
	
}