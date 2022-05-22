package com.anabada.anabada_api.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@Component
public class DeliveryScheduler {
    private final Job job;
    private final JobLauncher jobLauncher;

    @Scheduled(fixedDelay = 4320000)
//    @Scheduled(fixedDelay = 5000)
    public void startJob(){
        try{
            Map<String, JobParameter> jobParameterMap = new HashMap<>();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date time = new Date();

            String timeInstance = format.format(time);
            jobParameterMap.put("requestDate", new JobParameter(timeInstance));
            JobParameters parameters = new JobParameters(jobParameterMap);

            JobExecution jobExecution = jobLauncher.run(job, parameters);
        }catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                JobParametersInvalidException e){
            e.printStackTrace();
        }

    }
}














