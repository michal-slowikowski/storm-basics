package storm.helloworld.main;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.helloworld.bolt.WordCounter;
import storm.helloworld.bolt.WordNormalizer;
import storm.helloworld.spout.WordReader;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class TopologyMain {
	
	private static Logger logger = LoggerFactory.getLogger(TopologyMain.class);
	
	public static void main(String[] args) throws InterruptedException {
		
		// Topology definition
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("word-reader", new WordReader(), 1);
		builder.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader");
		builder.setBolt("word-counter", new WordCounter(), 1).fieldsGrouping( "word-normalizer", new Fields("word"));
		
		// Configuration
		Config conf = new Config();
		conf.put("wordsFile", getResourceFileName(args));
		conf.setDebug(false);
		
		// Topology run
        conf.put(Config.TOPOLOGY_DEBUG, false);
        conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 5000);		
        
        logger.info("** Starting topology **");
        if (isLocalModeNeeded()){
        	
        	LocalCluster cluster = new LocalCluster();
    		cluster.submitTopology("Getting-Started-Toplogie", conf, builder.createTopology());
    		Thread.sleep(1000);

        } else {
        	
    		try {
    			StormSubmitter.submitTopology("Getting-Started-Toplogie", conf, builder.createTopology());
    		} catch (AlreadyAliveException | InvalidTopologyException e) {
    			logger.error("Error while sumitting topology ",e);
    		}
    		
        }
        
	}
	
	private static String getResourceFileName(String[] args){
		String path = "";

		if (checkIfFileExist(args)){
			path = args[0]; 
		} else {
			path = ClassLoader.getSystemClassLoader().getResource("StandardTestWords.txt").getPath();
		}
		
		return path;
	}

	private static boolean checkIfFileExist(String[] args) {
		boolean fileExist = false;
		
		if (args.length > 0 && !StringUtils.trim(args[0]).equals("")){
			File resourceFile = new File(args[0]);
			if (resourceFile.exists()){
				fileExist = true;
			}
		}
		
		return  fileExist;
	}
	private static boolean isLocalModeNeeded(){
		return Boolean.parseBoolean(System.getProperties().getProperty("localMode"));
	}
	
}