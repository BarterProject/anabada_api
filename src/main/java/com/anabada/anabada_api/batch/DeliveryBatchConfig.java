package com.anabada.anabada_api.batch;

import com.anabada.anabada_api.domain.delivery.dto.DeliveryTrackingDTO;
import com.anabada.anabada_api.domain.delivery.entity.DeliveryVO;
import com.anabada.anabada_api.domain.delivery.repository.DeliveryRepository;
import com.anabada.anabada_api.domain.delivery.service.DeliveryFindService;
import com.anabada.anabada_api.domain.delivery.service.DeliveryUpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeliveryBatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryFindService deliveryFindService;
    private final DeliveryUpdateService deliveryUpdateService;

    @Bean(name = "deliveryTerminateJob")
    public Job deliveryTerminateJob(Step deliveryTerminateStep) {
        return jobBuilderFactory.get("deliveryTerminateJob")
                .incrementer(new RunIdIncrementer())
                .start(deliveryTerminateStep)
                .build();
    }


    @JobScope
    @Bean(name = "deliveryTerminateStep")
    public Step deliveryTerminateStep(
            ItemReader deliveryReader,
            ItemProcessor deliveryProcessor,
            ItemWriter deliveryWriter
    ) {

        TaskletStep wow = stepBuilderFactory.get("deliveryTerminateStep")
                .<DeliveryVO, DeliveryVO>chunk(10)
                .reader(deliveryReader)
                .processor(deliveryProcessor)
                .writer(deliveryWriter)
                .build();
        System.out.println(wow);
        return wow;
    }

    @StepScope
    @Bean
    public RepositoryItemReader<DeliveryVO> deliveryReader(@Value("#{jobParameters[urquestDate]}") String requestDate) {
        log.info("Spring Batch: " + requestDate);
        return new RepositoryItemReaderBuilder<DeliveryVO>()
                .name("deliveryReader")
                .repository(deliveryRepository)
                .methodName("findByStateAndTrackingNumber")
                .pageSize(10)
                .arguments(List.of(DeliveryVO.STATE.EXCHANGING.ordinal()))
                .sorts(Collections.singletonMap("idx", Sort.Direction.DESC))
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<DeliveryVO, DeliveryVO> deliveryProcessor() {
        return delivery -> {
            System.out.println(delivery);
            String trackingNumber = delivery.getTrackingNumber();
            DeliveryTrackingDTO result = deliveryFindService.getTracking(trackingNumber);

            if(result.getComplete().equals("true"))
                delivery.completeDelivery();

            return delivery;
        };
    }

    @StepScope
    @Bean
    public ItemWriter<DeliveryVO> deliveryWriter(){
        return deliveries -> {
            deliveries.forEach(i -> {
                if(i.getState() == DeliveryVO.STATE.TERMINATED.ordinal())
                    deliveryUpdateService.save(i);
            });
        };
    }

}

















