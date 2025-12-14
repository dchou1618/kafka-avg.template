import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.DoubleSerializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties
import java.util.Random

object Producer {
    fun runProducer() {
        val props =
            Properties().apply {
                put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
                put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DoubleSerializer::class.java.name)
                put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
            }
        val random = java.util.Random()

        KafkaProducer<String, Double>(props).use { producer ->
            while (true) {
                val value = random.nextGaussian() // Random N(0,1)
                val record = ProducerRecord<String, Double>("numbers", null, value)
                producer.send(record) { metadata, exception ->
                    if (exception != null) {
                        println("Error producing: ${exception.message}")
                    } else {
                        println("Produced value $value to partition ${metadata.partition()}, offset ${metadata.offset()}")
                    }
                }
                Thread.sleep(500)
            }
        }
    }
}
