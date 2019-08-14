package com.lin.huffman;

import java.util.*;

/**
 * 用户linji
 * 邮箱：lin15666067507
 * 编码时间：2019/8/14
 */
public class HuffmanTree {
    //生成霍夫曼树编码
    Map<Byte, String> codes = new HashMap<Byte, String>();
    StringBuilder stringBuilder = new StringBuilder();

    /**
     * 获得哈夫曼编码
     *
     * @param node          哈夫曼树
     * @param code          末位编码
     * @param stringBuilder 前置编码
     */
    public void getCodes(Node node, String code, StringBuilder stringBuilder) {
        StringBuilder stringBuilder2 = new StringBuilder(stringBuilder);
        stringBuilder2.append(code);

        if (node != null) {
            if (node.data == null) {
                getCodes(node.left, "0", stringBuilder2);
                getCodes(node.right, "1", stringBuilder2);
            } else {
                codes.put(node.data, stringBuilder2.toString());
            }
        } else return;


    }

    /**
     * 获取哈夫曼树节点数组
     *
     * @param str 字符串
     * @return
     */
    private List<Node> getNodes(String str) {
        byte[] contentbytes = str.getBytes();

        ArrayList<Node> nodes = new ArrayList<Node>();

        //统计字符出现次数
        Map<Byte, Integer> counts = new HashMap<Byte, Integer>();
        for (byte b : contentbytes) {
            Integer count = counts.get(b);
            if (count == null) { //map中还没有此数据
                counts.put(b, 1);
            } else {
                counts.put(b, count + 1);
            }
        }
        //把每个键值转化为Node对象并加入List
        for (Map.Entry<Byte, Integer> entry : counts.entrySet()) {
            nodes.add(new Node(entry.getKey(), entry.getValue()));
        }

        //System.out.println(nodes);
        return nodes;
    }

    /**
     * 创建哈夫曼树
     *
     * @param str
     * @return
     */
    public Node creatHuffmanTree(String str) {
        List<Node> nodes = getNodes(str);

        while (nodes.size() > 1) {
            //排序
            Collections.sort(nodes);

            //取出最小的两颗二叉树
            Node leftNode = nodes.get(0);
            Node rightNode = nodes.get(1);

            Node parent = new Node(null, leftNode.weight + rightNode.weight);
            parent.left = leftNode;
            parent.right = rightNode;

            nodes.remove(leftNode);
            nodes.remove(rightNode);
            nodes.add(parent);
        }
        //nodes.get(0).preOrder();
        return nodes.get(0);
    }

    /**
     * 压缩字符串
     *
     * @param bytes
     * @param codes
     * @return
     */
    public byte[] zip(byte[] bytes, Map<Byte, String> codes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(codes.get(b));
        }

        while (stringBuilder.length()%8 != 0 ) {
            stringBuilder.append("0");
        }
        int len = stringBuilder.length() / 8;
        byte[] bytes2 = new byte[len];
        for (int i = 0; i < len; i++) {
            if ((i + 1) << 3 > stringBuilder.length()) {
                String strByte = stringBuilder.substring(i << 3, stringBuilder.length());

                bytes2[i] = (byte) Integer.parseInt(strByte, 2);
            } else {
                String strByte = stringBuilder.substring(i << 3, (i + 1) << 3);
                bytes2[i] = (byte) Integer.parseInt(strByte, 2);
            }
        }

        System.out.println("压缩后: " + stringBuilder);

        return bytes2;
    }

    /**
     * byte转换String
     *
     * @param flag
     * @param b
     * @return
     */
    public String byteToString(boolean flag, byte b) {
        int code = b;

        if (flag) {
            code = code | 256;
        }
        String str = Integer.toBinaryString(code);

        return str.substring(str.length() - 8);

    }

    /**
     * 解压
     *
     * @param bytes
     * @param codes
     * @return
     */
    public byte[] reZip(byte[] bytes, Map<Byte, String> codes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(byteToString(b >= 0, b));
        }

        //逆转map
        Map<String, Byte> map = new HashMap<String, Byte>();
        for (Map.Entry<Byte, String> entry : codes.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        String str = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());

        //解码
        int start = 0;
        int l = 1;
        String strTemp = "";
        List<Byte> byteEnd =new ArrayList<Byte>();
        while (start + l < str.length()) {
            strTemp = str.substring(start, start + l);
            if ( map.get(strTemp) != null) {
                byteEnd.add( map.get(strTemp));
                start += l;
                l = 1;
            }else {
                l++;
            }
        }

        byte[] bs = new byte[byteEnd.size()];
        for (int i = 0; i < bs.length; i++) {
            bs[i] = byteEnd.get(i);
        }
        return bs;
    }


    public static void main(String[] args) {
        HuffmanTree huffmanTree = new HuffmanTree();
        String str = "abcdefgh igklm nopqsdhcshd 测试数据 cigasyud xfsa tyfxtyascdxr";
        System.out.println("原数据为: " + str);
        System.out.println("压缩前大小: " + str.getBytes().length + "byte");

        //压缩
        Node node = huffmanTree.creatHuffmanTree(str);
        huffmanTree.getCodes(node, "", huffmanTree.stringBuilder);
        Map<Byte, String> codes = huffmanTree.codes;
        byte[] bytes = huffmanTree.zip(str.getBytes(), codes);
        System.out.println("压缩后大小:" + bytes.length + "byte");
        System.out.println(Arrays.toString(bytes));
        //解压缩
        System.out.println("数据恢复后:" + new String(huffmanTree.reZip(bytes,codes)));


    }
}
