package com.fpoly.datn.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fpoly.datn.common.CommonUtils;
import com.fpoly.datn.entity.EmailJob;
import com.fpoly.datn.repository.ConfigRepo;
import com.fpoly.datn.repository.EmailJobRepo;

@Component
public class EmailJobScheduler {

	@Autowired
	private ConfigRepo configRepo;
	@Autowired
	private EmailJobRepo emailJobRepo;

	@Scheduled(initialDelay = 10000, fixedRate = 30 * 1000)
	public void syncData() {

		System.out
				.println("------Starting: Email job " + CommonUtils.StringFormatDate(new Date(), "dd/MM/yyy hh:MM:ss"));
		// get tham so
		String port = configRepo.getByName("mail.smtp.port").getValue();
		String emailRoot = configRepo.getByName("email_root").getValue();
		String passRoot = configRepo.getByName("pass_root").getValue();
		try {
			ExecutorService executorService = Executors.newFixedThreadPool(100);

			// find list email
			List<EmailJob> emailJobs = emailJobRepo.findByStatus(0);
			if (emailJobs.size() > 0) {
				System.out.println("----> email size: " + emailJobs.size());
				List<Future<EmailJobTask>> emailJobTaskFutureList = new ArrayList<Future<EmailJobTask>>();
				for (EmailJob email : emailJobs) {
					EmailJobTask task = new EmailJobTask(port, emailRoot, passRoot, email);
					emailJobTaskFutureList.add(executorService.submit(task));
				}

				if (emailJobTaskFutureList.size() > 0) {
					for (Future<EmailJobTask> taskFuture : emailJobTaskFutureList) {
						EmailJobTask task = taskFuture.get();
						try {
							if (task.isError()) {
								EmailJob emailJob = task.getEmailJob();
								if (emailJob != null) {
									emailJobRepo.save(emailJob);
								}
							} else {
								EmailJob emailJob = task.getEmailJob();
								if (emailJob != null) {
									emailJobRepo.save(emailJob);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("------Finish: email job " + CommonUtils.StringFormatDate(new Date(), "dd/MM/yyy hh:MM:ss"));
	}
}
