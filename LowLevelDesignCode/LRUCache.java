package DSA;

import java.util.HashMap;

public class LRUCache {

	class Node {
		Node prev, next;
		int key,value;
		
		Node(int _key, int _value)
		{
			key = _key;
			value = _value;
		}
	}
	
	Node head = new Node(0,0);
	Node tail = new Node(0,0);
	HashMap<Integer, Node> map = new HashMap();
	int capacity;
	
	public LRUCache(int _capacity) {
		capacity = _capacity;
		head.next = tail;
		tail.prev = head;
	}
	
	
	public void put(int key, int value)
	{
		if(map.containsKey(key)) {
			remove(map.get(key));
		}
		if(map.size()==capacity) {
			remove(tail.prev);
		}
		insert(new Node(key,value));
	}
	
	private void insert(Node node) {
		map.put(node.key, node);
		node.next = head.next;
		node.next.prev = node;
		head.next = node;
		node.prev = head;	}


	private void remove(Node node) {
		map.remove(node.key);
		node.prev.next = node.next;
		node.next.prev = node.prev;		
	}
	
	public int get(int key)	{
		if(map.containsKey(key)) {
			Node node = map.get(key);
			remove(node);
			insert(node);
			return node.value;
		}
		else
			return -1;
	}
	
	public void display() {
		Node n1 = head.next;
		while(n1.next!=null) {
			System.out.println(n1.key);
			n1 = n1.next;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LRUCache lru = new LRUCache(3);
		lru.put(1, 10);
		lru.put(2, 20);
		lru.put(3, 30);
		lru.get(2);
		lru.put(4, 40);
		System.out.println("get of 1 : " + lru.get(1));
		lru.display();
		

	}

}
