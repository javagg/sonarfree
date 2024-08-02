package pw.nyacat;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException, InterruptedException {
        String agentPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//        System.out.println(agentPath);
        String web_pid = "";
        String ce_pid = "";
//        boolean hook_web = false;
//        boolean hook_ce = false;
//        while (!hook_ce && !hook_web) {
//            List<VirtualMachineDescriptor> virtualMachineDescriptorList = VirtualMachine.list();
//            for (VirtualMachineDescriptor vmd : virtualMachineDescriptorList) {
//                if (vmd.displayName().startsWith("org.sonar.server.app.WebServer")) {
//                    System.out.println(vmd.displayName() + " pid: " + vmd.id() + " hook with: " + agentPath);
//                    VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
//                    virtualMachine.loadAgent(agentPath);
//                    virtualMachine.detach();
//                    hook_web = true;
//                }
//                if (vmd.displayName().startsWith("org.sonar.ce.app.CeServer")) {
//                    System.out.println(vmd.displayName() + " pid: " + vmd.id() + " hook with: " + agentPath);
//                    VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
//                    virtualMachine.loadAgent(agentPath);
//                    virtualMachine.detach();
//                    hook_ce = true;
//                }
//            }
//        }
        while (true) {
            List<VirtualMachineDescriptor> virtualMachineDescriptorList = VirtualMachine.list();
            for (VirtualMachineDescriptor vmd : virtualMachineDescriptorList) {
                if (vmd.displayName().contains("org.sonar.server.app.WebServer") && !vmd.id().equals(web_pid)) {
                    System.out.println(vmd.displayName() + " pid: " + vmd.id() + " hook with: " + agentPath);
                    VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                    virtualMachine.loadAgent(agentPath);
                    virtualMachine.detach();
                    web_pid = vmd.id();
                }
                if (vmd.displayName().contains("org.sonar.ce.app.CeServer") && !vmd.id().equals(ce_pid)) {
                    System.out.println(vmd.displayName() + " pid: " + vmd.id() + " hook with: " + agentPath);
                    VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                    virtualMachine.loadAgent(agentPath);
                    virtualMachine.detach();
                    ce_pid = vmd.id();
                }
            }
            if (web_pid.equals("") || ce_pid.equals("")) {
                System.out.println("waiting target jvm...");
                Thread.sleep(1000);
            } else {
                System.out.println("pending jvm restart...");
                Thread.sleep(1500);
            }
        }
    }
}
