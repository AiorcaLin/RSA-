import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;//生成密钥对
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * RSA 公钥加密，私钥解密
 */
public class RSA_UI{

    /**
     * 加密
     *
     * @param key KEY
     * @param in  输入参数
     * @param out 输出加密后的密文
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static void encode(Key key, InputStream in, OutputStream out) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {
        // 最大的加密明文长度
        final int maxEncryptBlock = 245;//改

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //，静态内部类我们不需要创建外部类来访问，可以直接访问它：
        //密钥工厂能够将从外部来源（如文件或数据库）读取的密钥数据（通常是以编码格式）转换为适用于加密操作的PublicKey或PrivateKey对象。
        //getInstance("RSA"): 构造，获取实例，这是一个静态方法，它返回一个KeyFactory对象，该对象能够将编码的密钥数据转换为RSA算法所使用的密钥对象。在这里，我们指定了"RSA"算法，这意味着返回的KeyFactory实例能够处理RSA算法的密钥。

        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        //Cipher: 这是一个提供加密和解密功能的类。它可以用于加密数据、解密数据以及其他与加密相关的操作。
        //keyFactory.getAlgorithm()将返回"RSA"。
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //初始化Cipher为加密模式

        byte[] buffer = new byte[maxEncryptBlock];//定义了一个变量 245
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            //in.read(buffer) 方法从输入流中读取数据，并将其存储到 buffer 中，返回读取的字节数。如果读取到流的末尾，则返回 -1
            out.write(cipher.doFinal(buffer, 0, len));
            //cipher.doFinal(buffer, 0, len) 方法对读取的数据块进行加密。buffer 是包含明文数据的字节数组，0 是起始偏移量，len 是要加密的字节数。
            //out.write(cipher.doFinal(buffer, 0, len)) 将加密后的数据写入输出流。
        }
    }

    /**
     * 解密
     *
     * @param key KEY
     * @param in  输入参数
     * @param out 输出解密后的原文
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static void decode(Key key, InputStream in, OutputStream out) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IOException, IllegalBlockSizeException, BadPaddingException {

        // 最大的解密密文长度//改
        final int maxDecryptBlock = 256;

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, key);
        //初始化为解密模式

        byte[] buffer = new byte[maxDecryptBlock];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            out.write(cipher.doFinal(buffer, 0, len));
        }
    }

    public static void file(){
        try {
            BufferedReader in = new BufferedReader(new FileReader("test.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                System.out.println(str);
            }
            System.out.println(str);
        } catch (IOException e) {
        }
    }


    public static void block(String str, BufferedWriter out, RSAPublicKey rsaPublicKey, RSAPrivateKey rsaPrivateKey) throws Exception{
        //System.out.println(((RSAPrivateKey) keyPair.getPrivate()).);

        // 要加密的原文
        byte[] content = str.getBytes(StandardCharsets.UTF_8);
        // 这个方法的作用是将字符串转换为字节数组，使用的字符集是UTF-8编码。，字符串是以Unicode字符表示的，而字节数组则是以字节表示的

        System.out.println("明文：" + new String(content, StandardCharsets.UTF_8));
        //一个字节数组转换回字符串形式

        // 加密后的密文
        ByteArrayOutputStream encryptedout = new ByteArrayOutputStream();//容器
        // 公钥加密
        encode(rsaPublicKey, new ByteArrayInputStream(content), encryptedout);//公钥 明文 密文1

        String encoded = Base64.getEncoder().encodeToString(encryptedout.toByteArray());
        System.out.println("密文：" + encoded);
        out.write(encoded);
        out.write("\n");
        //Base64.getEncoder() 返回一个Base64编码器对象。
        //encodeToString(encryptedout.toByteArray()) 将字节数组 encryptedout.toByteArray() 使用Base64编码并转换成字符串形式。

        // 解密后的原文
        ByteArrayOutputStream decryptedOut = new ByteArrayOutputStream();
        // 私钥解密
        decode(rsaPrivateKey, new ByteArrayInputStream(encryptedout.toByteArray()), decryptedOut);

        //System.out.println("解密后的原文：" + new String(decryptedOut.toByteArray(), StandardCharsets.UTF_8));
    }
    //解密
    public  static  void  defile(String src,String opt) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        //第一行私钥，第二行模数，文件内容，第三行输出
        BufferedWriter out = new BufferedWriter(new FileWriter(opt));
        BufferedReader in = new BufferedReader(new FileReader(src));

        String input = in.readLine(); // Read the input as a string
        byte[] keyBytes = input.getBytes(StandardCharsets.UTF_8); // Convert the string to bytes
        String n=in.readLine();
        byte[] mod = n.getBytes(StandardCharsets.UTF_8);
        // Assuming the input string is in PKCS#8 format
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey rsaPrivateKey = keyFactory.generatePrivate(keySpec);
        ByteArrayOutputStream decryptedOut = new ByteArrayOutputStream();
        String str = null;
        byte[] content = str.getBytes(StandardCharsets.UTF_8);
        while ((str = in.readLine()) != null) {
            System.out.println(str);
            decode(rsaPrivateKey, new ByteArrayInputStream(content), decryptedOut);
        }
    }

    //加密
    public static void main(String src,String opt) throws Exception {
        // 生成 RSA 密钥对
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        //密钥长度2048
        KeyPair keyPair = keyPairGenerator.generateKeyPair();//产生密钥对容器


        // 公钥和私钥
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

       // System.out.println("公钥：" + Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded()));
        //System.out.println("私钥：" + Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded()));
        System.out.println("模数：" + Base64.getEncoder().encodeToString(rsaPublicKey.getModulus().toByteArray()));
        //System.out.println("公钥: " + ((RSAPublicKey)rsaPublicKey).getModulus().bitLength() + " bits");
        System.out.println("私钥指数长度: " + rsaPrivateKey.getPrivateExponent().bitLength() + " bits");

        //一些其他数据p、q、n
        RSAPrivateCrtKey rsaPrivateKey1 = (RSAPrivateCrtKey) rsaPrivateKey;

        // 打印私钥的组成部分
        System.out.println("模数 (n): " + rsaPrivateKey1.getModulus());
        System.out.println("私钥指数 (d): " + rsaPrivateKey1.getPrivateExponent());
        System.out.println("公钥指数 (e): " + rsaPrivateKey1.getPublicExponent());
        System.out.println("素数 p: " + rsaPrivateKey1.getPrimeP());
        System.out.println("素数 q: " + rsaPrivateKey1.getPrimeQ());
        //System.out.println("d mod (p-1): " + rsaPrivateKey1.getPrimeExponentP());
        //System.out.println("d mod (q-1): " + rsaPrivateKey1.getPrimeExponentQ());
        //System.out.println("q 的逆元 mod p: " + rsaPrivateKey1.getCrtCoefficient());


        try {
            BufferedWriter out=new BufferedWriter(new FileWriter(opt));
            BufferedReader in = new BufferedReader(new FileReader(src));
            String str;
            //int count=0;
            while ((str = in.readLine()) != null) {
                System.out.println(str);
                block(str,out,rsaPublicKey,rsaPrivateKey);
            }

            System.out.println(str);
            in.close();
            out.close();
        } catch (IOException e) {
        }
    }

}
