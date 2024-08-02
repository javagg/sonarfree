package org.forfree.sonar

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.options.versionOption
import com.sonarsource.D.C.B
import org.apache.commons.codec.binary.Base64
import java.io.File
import java.io.FileOutputStream
import java.io.StringReader
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

object Constants {
   const val KEY_ALGO = "DSA"
   const val SIG_ALGO = "SHAwithDSA"
}

class Sonar : CliktCommand() {
    init {
        versionOption("1.0")
    }

    override fun run() {
    }
}

class Keypair: CliktCommand() {
    override fun run() {
        try {
            val gen = KeyPairGenerator.getInstance(Constants.KEY_ALGO)
            gen.initialize(1024)
            val pair = gen.genKeyPair()
            val privateKey = pair.private
            val publicKey = pair.public
            var ksp = PKCS8EncodedKeySpec(publicKey.encoded)
            var fos = FileOutputStream("public.key")
            fos.write(ksp.encoded)
            fos.close()
            ksp = PKCS8EncodedKeySpec(privateKey.encoded)
            fos = FileOutputStream("private.key")
            fos.write(ksp.encoded)
            fos.close()
            echo("Keys generated")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}

class License: CliktCommand() {
    override fun run() {
    }
}

class Verify:  CliktCommand() {
    private val pubKey: String by option("--key", "-k", help = "A java dsa public key file").required()
    private val inPath: String by option("--lic", "-l", help = "A signed lic file").required()

    override fun run() {
        val data = File(inPath).readText()
        val content = Base64.decodeBase64(data.toByteArray(StandardCharsets.UTF_8))
        val b = builderFromProps(String(content, StandardCharsets.UTF_8))
        val lic = b.A().get()
        val spec = File(pubKey).readBytes()
        val valid = verify(lic, spec)
        if (valid) {
            echo("License is valid")
        } else {
            echo("License is invalid")
        }
    }
}

class Generate: CliktCommand() {
    private val keyFile: String by option("--key", "-k", help = "private key file").required()
    private val inPath: String by option("--props", "-p", help = "java property file").required()

    override fun run() {
        val data =  File(inPath).readText()
        val b = builderFromProps(data)
        val unsigned = b.A().get()
        val spec = File(keyFile).readBytes()
        try {
            val key = KeyFactory.getInstance(Constants.KEY_ALGO).generatePrivate(PKCS8EncodedKeySpec(spec))
            val signature = Signature.getInstance(Constants.SIG_ALGO)
            signature.initSign(key)
            signature.update(unsigned.L().toByteArray(StandardCharsets.UTF_8))
            val hash = signature.sign()
            val digest = java.util.Base64.getEncoder().encodeToString(hash)
            b.E(digest)
            val signed = b.A().get()
            val content = signed.F()
            echo(content)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun builderFromProps(content: String): com.sonarsource.D.C.B._A {
    val props = Properties()
    props.load(StringReader(content))
    val builder = com.sonarsource.D.C.B._A()
    builder
        .C(props.getProperty(com.sonarsource.D.C.B.N)) // Company
        .B(props.getProperty(com.sonarsource.D.C.B.C)) // Edition
        .I(props.getProperty(com.sonarsource.D.C.B.S)) // EditionLabel
        .H(props.getProperty(com.sonarsource.D.C.B.M)) // Expiration
        .G(props.getProperty(com.sonarsource.D.C.B.B)) // Type
        .E(props.getProperty(com.sonarsource.D.C.B.A)) // Digest
        .A(props.getProperty(com.sonarsource.D.C.B.X)) // ServerId
        .F(props.getProperty(com.sonarsource.D.C.B.F)) // Features
        .A(props.getProperty(com.sonarsource.D.C.B.R).toLong()) // MaxLoc
        .A(props.getProperty(com.sonarsource.D.C.B.H).split(","))
        .A(props.getProperty(com.sonarsource.D.C.B.J).toBooleanStrict()) // Support
    return builder
}

fun verify(lic: B, key: ByteArray): Boolean {
    try {
        val data = Base64.decodeBase64(lic.I().toByteArray(StandardCharsets.UTF_8))
        val pubkey = KeyFactory.getInstance(Constants.KEY_ALGO).generatePublic(X509EncodedKeySpec(key))
        val signature = Signature.getInstance(Constants.SIG_ALGO)
        signature.initVerify(pubkey)
        signature.update(lic.L().toByteArray(StandardCharsets.UTF_8))
        return signature.verify(data)
    } catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

object App {
    @JvmStatic
    fun main(args: Array<String>) = Sonar().subcommands(License().subcommands(Generate(), Verify()), Keypair()).main(args)
}
