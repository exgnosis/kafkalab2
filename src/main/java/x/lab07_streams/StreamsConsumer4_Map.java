/// Filter out the streams
package x.lab07_streams;

import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Printed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import x.utils.ClickstreamData;
import x.utils.MyConfig;

public class StreamsConsumer4_Map {
	private static final Logger logger = LoggerFactory.getLogger(StreamsConsumer4_Map.class);

	public static void main(String[] args) {

		Properties config = new Properties();
		// "bootstrap.servers" = "localhost:9092"
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, MyConfig.DEFAULT_BOOTSTRAP_SERVERS);
		config.put("group.id", "streaming4");
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams-map");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		// Records should be flushed every 10 seconds. This is less than the
		// default
		// in order to keep this example interactive.
		config.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
		// For illustrative purposes we disable record caches
		config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);

		final StreamsBuilder builder = new StreamsBuilder();

		final KStream<String, String> clickstream = builder.stream("clickstream");

		// clickstream.print(Printed.toSysOut());

		/*-
		we are transforming the data using a MAP operation
		input::
			key=domain (String),
			value = JSON (String)
		            {"timestamp":1451635200005,"session":"session_251","domain":"facebook.com","cost":91,"user":"user_16","campaign":"campaign_5","ip":"ip_67","action":"clicked"}
		
		mapped output:
		    key = action (String)
		    value = 1 (Integer) (used for counting / aggregating later)
		 */

		final Gson gson = new Gson();
		final KStream<String, Integer> actionStream = clickstream
				.map(new KeyValueMapper<String, String, KeyValue<String, Integer>>() {
					public KeyValue<String, Integer> apply(String key, String value) {
						try {
							logger.debug("map() : got key: " + key + ", value: " + value);
							ClickstreamData clickstreamData = gson.fromJson(value, ClickstreamData.class);

							String action = "???";

							/*-
							  # TODO-1 : extract action from 'clickstreamData' data (clickstream.action)
							  
							  # TODO-2 : set action to "unknown" if clickstream.action is null
							   
							  Something like this will work:
							   
							 		String action = (clickstreamData.action != null) &&
							 						(!clickstreamData.action.isEmpty()) ? clickstreamData.action : "unknown";
							 */

							KeyValue<String, Integer> actionKV = null;

							/*-
							 # TODO-3 : construct a new KeyValue as follows
							 			key = action
										value = 1
							   like this:
										actionKV = new KeyValue<>(action, 1);
							 */

							logger.debug("map() : returning : " + actionKV);
							return actionKV;
						} catch (Exception ex) {
							logger.error("", ex);
							return new KeyValue<String, Integer>("unknown", 1);
						}
					}
				});

		actionStream.print(Printed.toSysOut());

		// start the stream
		final KafkaStreams streams = new KafkaStreams(builder.build(), config);
		streams.cleanUp();
		streams.start();

		logger.info("kstreams starting on " + MyConfig.TOPIC_CLICKSTREAM);

		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

	}

}
