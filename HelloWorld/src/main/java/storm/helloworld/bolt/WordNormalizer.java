package storm.helloworld.bolt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.helloworld.bolt.template.AbstractBolt;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class WordNormalizer extends AbstractBolt{

	private static final long serialVersionUID = 462960109656327405L;
	private OutputCollector collector;
	private Logger logger = LoggerFactory.getLogger(WordNormalizer.class);

	public void execute(Tuple input) {
		logger.info("** Normalizing Tuple **");		
		String sentence = input.getString(0);
		String[] words = sentence.split(" ");
		for (String word : words) {
			word = word.trim();
			if (!word.isEmpty()) {
				word = word.toLowerCase();
				// Emit the word
				List<Tuple> a = new ArrayList<Tuple>();
				a.add(input);
				collector.emit(a, new Values(word));
			}
		}
		// Acknowledge the tuple
		collector.ack(input);
	}

	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}


	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}


}