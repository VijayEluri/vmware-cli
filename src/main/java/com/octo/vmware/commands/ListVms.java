package com.octo.vmware.commands;

import java.util.List;

import vim25.VirtualMachinePowerState;
import vim25.VirtualMachineToolsStatus;

import com.octo.vmware.ICommand;
import com.octo.vmware.entities.VmInfo;
import com.octo.vmware.services.VmsListService;
import com.octo.vmware.utils.VimServiceUtil;

public class ListVms implements ICommand {

	public void execute(IOutputer outputer, String [] args) throws Exception {
		if (args.length != 1) {
			throw new SyntaxError();
		}
		String esxName = args[0];
		VimServiceUtil vimServiceUtil = VimServiceUtil.get(esxName);
		List<VmInfo> vmsList = VmsListService.getVmsList(vimServiceUtil);

		outputer.log("Found " + vmsList.size() + " VM(s) on " + esxName);
		outputer.output(vmsList, vimServiceUtil, new IObjectOutputer<List<VmInfo>>() {

			public void output(IOutputer outputer, VimServiceUtil vimServiceUtil, List<VmInfo> vmsList) {
				if (vmsList.size() > 0) {
					outputer.log(String.format("%-20s %-30s %11s %-9s %-40s", "Resource Pool", "VM Name", "Status", "CPU/RAM", "Guest"));
					outputer.log("--------------------------------------------------------------------------------------");
					for (VmInfo info : vmsList) {
						outputer.log(String.format("%-20s %-30s %11s %2d %6d %s", info.getResourcePool().getName(), info.getName(), info.getStatus(), info.getCpu(), info.getRam(), formatGuest(info)));
					}
				}
			}
			
			private String formatGuest(VmInfo vmInfo) {
				if (VirtualMachineToolsStatus.TOOLS_OK.toString().equals(vmInfo.getGuestToolsStatus())) {
					return vmInfo.getGuestToolsStatus() + " (" + vmInfo.getGuestIp() + " : " + vmInfo.getGuestHostname() + ")";
				}
				else {
					return VirtualMachinePowerState.POWERED_ON.toString().equals(vmInfo.getStatus()) ? vmInfo.getGuestToolsStatus() : "";
				}
			}
			
		});
		
		
	}

	public String getSyntax() {
		return "esx_name"; 
	}

	public String getHelp() {
		return "list virtual machiness of an esx server";
	}

	public String getCommand() {
		return "list";
	}
	
	public Target getTarget() {
		return Target.ESX;
	}

}
