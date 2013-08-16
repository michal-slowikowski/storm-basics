package storm.helloworld.bolt;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.helloworld.bolt.template.AbstractBolt;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.tuple.Tuple;

public class WordCounter extends AbstractBolt{

	private static final long serialVersionUID = 4289414537465400128L;
	private Integer id;
	private String name;
	private Map<String, Integer> counters;
	private OutputCollector collector;
	private Logger logger =  LoggerFactory.getLogger(WordCounter.class);

	@Override
	public void execute(Tuple input) {
		String str = input.getString(0);

		if (!counters.containsKey(str)) {
			counters.put(str, 1);
		} else {
			Integer c = counters.get(str) + 1;
			counters.put(str, c);
		}
		
		printStatistics();
		
		// Set the tuple as Acknowledge
		collector.ack(input);
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.counters = new HashMap<String, Integer>();
		this.collector = collector;
		this.name = context.getThisComponentId();
		this.id = context.getThisTaskId();
	}
	
	private void printStatistics() {
		logger.info("-- Word Counter [" + name + "-" + id + "] --");
		for (Map.Entry<String, Integer> entry : counters.entrySet()) {
			logger.info(entry.getKey() + ": " + entry.getValue());
		}
	}
	
}
