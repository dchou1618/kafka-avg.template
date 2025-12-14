import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.DoubleDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.Properties

object Consumer {
    fun runConsumer() {
        val props =
            Properties().apply {
                put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
                put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
                put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DoubleDeserializer::class.java.name)
                put(ConsumerConfig.GROUP_ID_CONFIG, "average-group")
                put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
            }

        KafkaConsumer<String, Double>(props).use { consumer ->
            consumer.subscribe(listOf("numbers"))
            var average: Double = 0.0
            var count: Int = 0

            while (true) {
                val records = consumer.poll(Duration.ofMillis(1000))
                for (record in records) {
                    average -= (average - record.value()) / (count + 1)
                    val avg = average
                    count++
                    println("Received ${record.value()}, running average: $avg")
                }
            }
        }
    }
}
