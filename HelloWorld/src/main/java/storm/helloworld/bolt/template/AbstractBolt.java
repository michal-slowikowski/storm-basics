package storm.helloworld.bolt.template;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public abstract class AbstractBolt implements IRichBolt {

	private static final long serialVersionUID = 5546612329358510388L;

	@Override
	public void cleanup() {
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
	
	@Override
	public abstract void prepare(Map stormConf, TopologyContext context,OutputCollector collector);

	@Override
	public abstract void execute(Tuple input);	

}
