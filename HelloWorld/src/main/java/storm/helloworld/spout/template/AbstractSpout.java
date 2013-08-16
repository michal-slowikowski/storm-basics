package storm.helloworld.spout.template;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;

public abstract class AbstractSpout implements IRichSpout{

	private static final long serialVersionUID = 8705384450857936481L;

	@Override
	public void close() {
		
	}

	@Override
	public void activate() {
		
	}

	@Override
	public void deactivate() {
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}	
	
	@Override
	public abstract void nextTuple();

	@Override
	public abstract void ack(Object msgId);

	@Override
	public abstract void fail(Object msgId);

	@Override
	public abstract void declareOutputFields(OutputFieldsDeclarer declarer);

	@Override
	public abstract void open(Map conf, TopologyContext context, SpoutOutputCollector collector);	
	
}
