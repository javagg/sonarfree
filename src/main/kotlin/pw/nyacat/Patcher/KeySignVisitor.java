package pw.nyacat.Patcher;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class KeySignVisitor extends ClassVisitor {
    public KeySignVisitor(ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if ((access & Opcodes.ACC_PRIVATE) == 0 && !name.equals("<init>") && desc.startsWith("(Lcom/sonarsource/") && desc.endsWith("[B)Z")) {
            System.out.println("hooked method: " + name + " with: " + desc);
            MethodVisitor mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
            return new LicenseCheckMethodVisitor(mv);
        }

        if (this.cv != null)
            return this.cv.visitMethod(access, name, desc, signature, exceptions);
        return null;
    }

    private static class LicenseCheckMethodVisitor extends MethodVisitor {
        private final MethodVisitor target;

        LicenseCheckMethodVisitor(MethodVisitor mv) {
            super(Opcodes.ASM9, null);
            this.target = mv;
        }

        @Override
        public void visitCode() {
//            mv.visitCode();
//            mv.visitVarInsn(ALOAD, 0);
//            mv.visitVarInsn(ALOAD, 1);
//            mv.visitVarInsn(ALOAD, 0);
//            mv.visitMethodInsn(INVOKESPECIAL, "com/sonarsource/D/B/E", "A", "()[B", false);
//            mv.visitMethodInsn(INVOKEVIRTUAL, "com/sonarsource/D/B/E", "A", "(Lcom/sonarsource/D/B/B;[B)Z", false);
//            mv.visitInsn(IRETURN);
//            mv.visitMaxs(3, 2);
//            mv.visitEnd();

            target.visitCode();
            target.visitVarInsn(Opcodes.ALOAD, 0);
            target.visitVarInsn(Opcodes.ALOAD, 1);
            target.visitInsn(Opcodes.ICONST_1);
            target.visitInsn(Opcodes.IRETURN);
            target.visitMaxs(2, 2);
            target.visitEnd();
        }
    }
}
