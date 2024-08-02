package pw.nyacat;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import pw.nyacat.Patcher.KeySignVisitor;
import pw.nyacat.Patcher.StringDecoder;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;


public class Agent {
    public static void premain(String agentOps, Instrumentation instrumentation) {
        instrument(instrumentation);
    }


    public static void agentmain(String agentOps, Instrumentation instrumentation) {
        instrument(instrumentation);
    }

    private static void instrument(Instrumentation instrumentation) {
        instrumentation.addTransformer(new PatcherTransformer());
    }


    private static class PatcherTransformer implements ClassFileTransformer {
        private int counter = 0;
        @Override
        public byte[] transform(ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            /*
            // hook string decode
            if (className.startsWith("com/sonarsource/D/B/A")) {
                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
                StringDecoder stringDecoder = new StringDecoder(classWriter);
                classReader.accept(stringDecoder, ClassReader.EXPAND_FRAMES);

                try {
                    System.out.println("/home/sonarqube/" + className.replace("/", ".") + ".class");
                    DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("/home/sonarqube/" + className.replace("/", ".") + ".class"));
                    dataOutputStream.write(classWriter.toByteArray());
                    dataOutputStream.flush();
                    dataOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return classWriter.toByteArray();
            }
            */

            if (classLoader.getName().equals("app") && className.startsWith("com/sonarsource/")) {
//                counter = counter +1;
//            }
//
//            if (className.startsWith("com/sonarsource/D/B/E") || className.startsWith("com/sonarsource/license/A/E")) {
//                System.out.println(counter);
//                System.out.println("Check point: " + classLoader.getName() + " cls: " + className);
//                System.out.println("Patcher found check point! " + className);
                ClassReader classReader = new ClassReader(classfileBuffer);
                ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
                KeySignVisitor keySignVisitor = new KeySignVisitor(classWriter);
                classReader.accept(keySignVisitor, ClassReader.EXPAND_FRAMES);

                // output patched class
//                try {
//                    System.out.println("/home/sonarqube/" + className.replace("/", ".") + ".class");
//                    DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("/home/sonarqube/" + className.replace("/", ".") + ".class"));
//                    dataOutputStream.write(classWriter.toByteArray());
//                    dataOutputStream.flush();
//                    dataOutputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                return classWriter.toByteArray();
            }
            return classfileBuffer;
        }
    }
}