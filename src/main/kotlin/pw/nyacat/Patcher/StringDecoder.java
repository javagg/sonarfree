package pw.nyacat.Patcher;

import org.objectweb.asm.*;

import static org.objectweb.asm.Opcodes.*;

public class StringDecoder extends ClassVisitor {
    public StringDecoder(ClassVisitor cv) {
        super(ASM9, cv);
    }


    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println(name + " with: " + desc);
        // patch string decoder check
        if ("<init>".equals(name) && "([J)V".equals(desc)) {
//            System.out.println("hooked method");
            MethodVisitor mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
            return new StringDecoderVisitor(mv);
        }

        if (this.cv != null)
            return this.cv.visitMethod(access, name, desc, signature, exceptions);
        return null;
    }

    private static class StringDecoderVisitor extends MethodVisitor {
        private final MethodVisitor target;

        StringDecoderVisitor(MethodVisitor mv) {
            super(ASM9, null);
            this.target = mv;
        }

        @Override
        public void visitCode() {
            target.visitCode();
            Label label0 = new Label();
            Label label1 = new Label();
            Label label2 = new Label();
            target.visitTryCatchBlock(label0, label1, label2, "java/io/UnsupportedEncodingException");
            Label label3 = new Label();
            target.visitLabel(label3);
            target.visitLineNumber(11, label3);
            target.visitVarInsn(ALOAD, 0);
            target.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            Label label4 = new Label();
            target.visitLabel(label4);
            target.visitLineNumber(12, label4);
            target.visitVarInsn(ALOAD, 1);
            target.visitInsn(ARRAYLENGTH);
            target.visitVarInsn(ISTORE, 2);
            Label label5 = new Label();
            target.visitLabel(label5);
            target.visitLineNumber(13, label5);
            target.visitIntInsn(BIPUSH, 8);
            target.visitVarInsn(ILOAD, 2);
            target.visitInsn(ICONST_1);
            target.visitInsn(ISUB);
            target.visitInsn(IMUL);
            target.visitIntInsn(NEWARRAY, T_BYTE);
            target.visitVarInsn(ASTORE, 3);
            Label label6 = new Label();
            target.visitLabel(label6);
            target.visitLineNumber(14, label6);
            target.visitVarInsn(ALOAD, 1);
            target.visitInsn(ICONST_0);
            target.visitInsn(LALOAD);
            target.visitVarInsn(LSTORE, 4);
            Label label7 = new Label();
            target.visitLabel(label7);
            target.visitLineNumber(15, label7);
            target.visitTypeInsn(NEW, "java/util/Random");
            target.visitInsn(DUP);
            target.visitVarInsn(LLOAD, 4);
            target.visitMethodInsn(INVOKESPECIAL, "java/util/Random", "<init>", "(J)V", false);
            target.visitVarInsn(ASTORE, 6);
            Label label8 = new Label();
            target.visitLabel(label8);
            target.visitLineNumber(17, label8);
            target.visitInsn(ICONST_1);
            target.visitVarInsn(ISTORE, 7);
            Label label9 = new Label();
            target.visitLabel(label9);
            target.visitFrame(F_FULL, 7, new Object[]{"com/sonarsource/D/B/A", "[J", INTEGER, "[B", LONG, "java/util/Random", INTEGER}, 0, new Object[]{});
            target.visitVarInsn(ILOAD, 7);
            target.visitVarInsn(ILOAD, 2);
            target.visitJumpInsn(IF_ICMPGE, label0);
            Label label10 = new Label();
            target.visitLabel(label10);
            target.visitLineNumber(18, label10);
            target.visitVarInsn(ALOAD, 6);
            target.visitMethodInsn(INVOKEVIRTUAL, "java/util/Random", "nextLong", "()J", false);
            target.visitVarInsn(LSTORE, 8);
            Label label11 = new Label();
            target.visitLabel(label11);
            target.visitLineNumber(19, label11);
            target.visitVarInsn(ALOAD, 1);
            target.visitVarInsn(ILOAD, 7);
            target.visitInsn(LALOAD);
            target.visitVarInsn(LLOAD, 8);
            target.visitInsn(LXOR);
            target.visitVarInsn(ALOAD, 3);
            target.visitIntInsn(BIPUSH, 8);
            target.visitVarInsn(ILOAD, 7);
            target.visitInsn(ICONST_1);
            target.visitInsn(ISUB);
            target.visitInsn(IMUL);
            target.visitMethodInsn(INVOKESTATIC, "com/sonarsource/D/B/A", "A", "(J[BI)V", false);
            Label label12 = new Label();
            target.visitLabel(label12);
            target.visitLineNumber(17, label12);
            target.visitIincInsn(7, 1);
            target.visitJumpInsn(GOTO, label9);
            target.visitLabel(label0);
            target.visitLineNumber(24, label0);
            target.visitFrame(F_CHOP, 1, null, 0, null);
            target.visitTypeInsn(NEW, "java/lang/String");
            target.visitInsn(DUP);
            target.visitVarInsn(ALOAD, 3);
            target.visitFieldInsn(GETSTATIC, "com/sonarsource/D/B/A", "B", "Ljava/lang/String;");
            target.visitMethodInsn(INVOKESPECIAL, "java/lang/String", "<init>", "([BLjava/lang/String;)V", false);
            target.visitVarInsn(ASTORE, 7);
            target.visitLabel(label1);
            target.visitLineNumber(27, label1);
            Label label13 = new Label();
            target.visitJumpInsn(GOTO, label13);
            target.visitLabel(label2);
            target.visitLineNumber(25, label2);
            target.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/io/UnsupportedEncodingException"});
            target.visitVarInsn(ASTORE, 8);
            Label label14 = new Label();
            target.visitLabel(label14);
            target.visitLineNumber(26, label14);
            target.visitTypeInsn(NEW, "java/lang/AssertionError");
            target.visitInsn(DUP);
            target.visitVarInsn(ALOAD, 8);
            target.visitMethodInsn(INVOKESPECIAL, "java/lang/AssertionError", "<init>", "(Ljava/lang/Object;)V", false);
            target.visitInsn(ATHROW);
            target.visitLabel(label13);
            target.visitLineNumber(29, label13);
            target.visitFrame(F_APPEND, 1, new Object[]{"java/lang/String"}, 0, null);
            target.visitVarInsn(ALOAD, 7);
            target.visitInsn(ICONST_0);
            target.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "indexOf", "(I)I", false);
            target.visitVarInsn(ISTORE, 8);
            Label label15 = new Label();
            target.visitLabel(label15);
            target.visitLineNumber(30, label15);
            target.visitVarInsn(ALOAD, 0);
            target.visitVarInsn(ILOAD, 8);
            target.visitInsn(ICONST_M1);
            Label label16 = new Label();
            target.visitJumpInsn(IF_ICMPEQ, label16);
            target.visitVarInsn(ALOAD, 7);
            target.visitInsn(ICONST_0);
            target.visitVarInsn(ILOAD, 8);
            target.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "substring", "(II)Ljava/lang/String;", false);
            Label label17 = new Label();
            target.visitJumpInsn(GOTO, label17);
            target.visitLabel(label16);
            target.visitFrame(F_FULL, 8, new Object[]{"com/sonarsource/D/B/A", "[J", INTEGER, "[B", LONG, "java/util/Random", "java/lang/String", INTEGER}, 1, new Object[]{"com/sonarsource/D/B/A"});
            target.visitVarInsn(ALOAD, 7);
            target.visitLabel(label17);
            target.visitFrame(F_FULL, 8, new Object[]{"com/sonarsource/D/B/A", "[J", INTEGER, "[B", LONG, "java/util/Random", "java/lang/String", INTEGER}, 2, new Object[]{"com/sonarsource/D/B/A", "java/lang/String"});
            target.visitFieldInsn(PUTFIELD, "com/sonarsource/D/B/A", "A", "Ljava/lang/String;");
            Label label18 = new Label();
            target.visitLabel(label18);
            target.visitLineNumber(31, label18);
            target.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            target.visitVarInsn(ALOAD, 1);
            target.visitMethodInsn(INVOKESTATIC, "java/util/Arrays", "toString", "([J)Ljava/lang/String;", false);
            target.visitVarInsn(ALOAD, 0);
            target.visitFieldInsn(GETFIELD, "com/sonarsource/D/B/A", "A", "Ljava/lang/String;");
            target.visitInvokeDynamicInsn("makeConcatWithConstants", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", new Handle(H_INVOKESTATIC, "java/lang/invoke/StringConcatFactory", "makeConcatWithConstants", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite;", false), new Object[]{"\u0001 with: \u0001"});
            target.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            Label label19 = new Label();
            target.visitLabel(label19);
            target.visitLineNumber(32, label19);
            target.visitInsn(RETURN);
            Label label20 = new Label();
            target.visitLabel(label20);
            target.visitLocalVariable("var8", "J", null, label11, label12, 8);
            target.visitLocalVariable("var7", "I", null, label9, label0, 7);
            target.visitLocalVariable("var11", "Ljava/lang/String;", null, label1, label2, 7);
            target.visitLocalVariable("var10", "Ljava/io/UnsupportedEncodingException;", null, label14, label13, 8);
            target.visitLocalVariable("this", "Lcom/sonarsource/D/B/A;", null, label3, label20, 0);
            target.visitLocalVariable("var1", "[J", null, label3, label20, 1);
            target.visitLocalVariable("var2", "I", null, label5, label20, 2);
            target.visitLocalVariable("var3", "[B", null, label6, label20, 3);
            target.visitLocalVariable("var4", "J", null, label7, label20, 4);
            target.visitLocalVariable("var6", "Ljava/util/Random;", null, label8, label20, 6);
            target.visitLocalVariable("var11", "Ljava/lang/String;", null, label13, label20, 7);
            target.visitLocalVariable("var12", "I", null, label15, label20, 8);
            target.visitMaxs(6, 10);
            target.visitEnd();
        }
    }

}
