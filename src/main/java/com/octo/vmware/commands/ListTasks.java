package com.octo.vmware.commands;

import java.util.List;

import com.octo.vmware.ICommand;
import com.octo.vmware.entities.ConverterTask;
import com.octo.vmware.services.ConverterTasksListService;
import com.octo.vmware.utils.ConverterServiceUtil;

public class ListTasks implements ICommand {

	public void execute(String[] args) throws Exception {
		ConverterServiceUtil converterServiceUtil = new ConverterServiceUtil();
		List<ConverterTask> list = ConverterTasksListService.getTaskList(converterServiceUtil);

		// Display output
		System.out.println("Found " + list.size() + " task(s).");
		if (list.size() > 0) {
			String header = String
					.format("%-40s %-40s %-10s %-10s", "Source VM", "Target VM", "Status", "Progress");
			System.out.println(header);
			System.out
					.println("--------------------------------------------------------------------------------------------------");
			for (ConverterTask converterTask : list) {
				System.out.println(String.format("%-40s %-40s %-10s %-10s", converterTask.getSource(),
						converterTask.getTarget(), converterTask.getStatus(), converterTask.getProgress()
								+ "%".toString()));
			}
		}

	}

	public String getCommandHelp() {
		return "list_tasks : lists all tasks of the converter";
	}

	public String getCommandName() {
		return "list_tasks";
	}

}