package com.iheleny.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * the element that hasn't been used for the longest time will be evicted from the cache.
 * some aspect of the cache:
 *  - All operations should run in order of O(1)
 *  - The cache has a limited size
 *  - It's mandatory that all cache operations support concurrency
 *  - If the cache is full, adding a new item must invoke the LRU strategy
 */
public class LRU {
    // LRU cache is a combination of a doubly linked list and a hashmap.
    // most recently used will be the head and least recently used will be the tail
    // when it reaches the capacity, remove the tail
    Node head;
    Node tail;
    Map<Integer, Node> map = new HashMap<>();
    int capacity;

    public LRU(int capacity) {
        this.capacity = capacity;
    }

    public Integer get(int key) {
        if (map.get(key) == null) {
            return null;
        }

        // move it to head
        Node target = map.get(key);
        detach(target);
        attach(target);
        return target.value;
    }

    public void put(int key, int value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;

            // move it to head
            detach(node);
            attach(node);
        } else {
            if (map.size() >= capacity) {
                // delete tail
                map.remove(key);
                detach(tail);
            }

            // add to head
            Node node = new Node(key, value);
            attach(node);
            map.put(key, node);
        }
    }

    private void detach(Node target) {
        if (target.prev != null) {
            target.prev.next = target.next;
        } else {
            head = target.next;
        }

        if (target.next != null) {
            target.next.prev = target.prev;
        } else {
            tail = target.prev;
        }
    }

    private void attach(Node target) {
        if (head != null) {
            head.prev = target;
        }

        target.next = head;
        target.prev = null;
        head = target;

        if (tail == null) {
            tail = head;
        }
    }

    static class Node {
        int key;
        int value;
        Node prev;
        Node next;

        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            sb.append("Node{ key=");
            sb.append(key);
            sb.append(", value=");
            sb.append(value);
            if (prev != null) {
                sb.append(", prev=[");
                sb.append(prev.key);
                sb.append(",");
                sb.append(prev.value);
            }
            if (next != null) {
                sb.append(", next=");
                sb.append(next.key);
                sb.append(",");
                sb.append(next.value);
            }
            sb.append("}");
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        LRU lru = new LRU(3);
        lru.get(1);
        System.out.println("head: " + (lru.head != null ? lru.head.toString() : null));
        System.out.println("tail: " + (lru.tail != null ? lru.tail.toString() : null));

        lru.put(1, 100);
        System.out.println("head: " + (lru.head != null ? lru.head.toString() : null));
        System.out.println("tail: " + (lru.tail != null ? lru.tail.toString() : null));
        lru.put(2, 45);
        System.out.println("head: " + (lru.head != null ? lru.head.toString() : null));
        System.out.println("tail: " + (lru.tail != null ? lru.tail.toString() : null));
        lru.put(3, 77);
        System.out.println("head: " + (lru.head != null ? lru.head.toString() : null));
        System.out.println("tail: " + (lru.tail != null ? lru.tail.toString() : null));
        lru.put(4, 111);
        System.out.println("head: " + (lru.head != null ? lru.head.toString() : null));
        System.out.println("tail: " + (lru.tail != null ? lru.tail.toString() : null));
    }
}


