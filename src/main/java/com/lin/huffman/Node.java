package com.lin.huffman;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户linji
 * 邮箱：lin15666067507
 * 编码时间：2019/8/14
 * <p>
 * 用于求字符出现频率为权值的霍夫曼数的节点
 */
public class Node implements Comparable<Node> {
    Byte data; //存放数据
    int weight; //权值
    Node left;
    Node right;

    public Node(Byte data, int weight) {
        this.data = data;
        this.weight = weight;
    }

    public int compareTo(Node o) {
        return this.weight - o.weight;
    }

    public String toString() {
        return "Node [data = " + data + ",weight = " + weight + "]";
    }


    //前序遍历
    public void preOrder() {
        System.out.println(this);
        if (this.left != null) {
            this.left.preOrder();
        }
        if (this.right != null) {
            this.right.preOrder();
        }
    }
}
