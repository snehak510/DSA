package DSA;

import java.util.HashMap;
import java.util.Map;

public class LFUCache {
	
	class DLLNode{
		DLLNode prev, next;
		int key,value;
		int frequency;
		
		public DLLNode(int _key, int _value) {
			key = _key;
			value = _value;
			this.frequency = 1;
		}
	}
	
	int capacity;
	int currSize, minFrequency;
	Map<Integer, DLLNode> cache;
	Map<Integer, DoubleLinkedList> frequencyMap;

	public LFUCache(int capacity) {
		this.capacity = capacity;
		this.currSize = 0;
		this.minFrequency = 0;
		this.cache = new HashMap<>();
		this.frequencyMap = new HashMap<>();
	}
	
	//get the value by key and then update the node frequency and relocate the node to new bucket
	public int get(int key) {
		DLLNode curNode = cache.get(key);
		if(curNode==null)	return -1;
		updateNode(curNode);
		return curNode.value;
	}
	
	private void updateNode(DLLNode curNode) {
		int curFreq = curNode.frequency;
		DoubleLinkedList curList = frequencyMap.get(curFreq);
		curList.removeNode((curNode));
		
		//if curr list the last list which has lowest feq and curr node is the only
		// node in that list remove the entire list and increase min freq by 1
		if(curFreq == minFrequency && curList.listSize==0) {
			minFrequency++;
		}
		curNode.frequency++;
		
		//add cur node to another list has curr freq +1
		// we dont have the list with this freq then initilise it
		DoubleLinkedList newList = frequencyMap.getOrDefault(curNode.frequency,new DoubleLinkedList());
		newList.addNode(curNode);
		frequencyMap.put(curNode.frequency,newList);		
	}
	
	public void put(int key,int value) {
		if(capacity==0)return;
		//corner case : check cache capacity initliazation
		if(cache.containsKey(key)) {
			DLLNode curNode = cache.get(key);
			curNode.value = value;
			updateNode(curNode);
		}
		else {
			currSize++;
			if(currSize>capacity) {
				//get minimum frequency list
				DoubleLinkedList minFreqList = frequencyMap.get(minFrequency);
				cache.remove(minFreqList.tail.prev.key);
				minFreqList.removeNode(minFreqList.tail.prev);
				currSize--;
			}
			
			//reset frequency to 1 because of adding new node
			minFrequency = 1;
			DLLNode newNode = new DLLNode(key,value);
			
			//get the list with frequency 1 and then add new node into the list and LFU cache
			DoubleLinkedList curList = frequencyMap.getOrDefault(1, new DoubleLinkedList());
			curList.addNode(newNode);
			frequencyMap.put(1, curList);
			cache.put(key, newNode);
		}
	}
	
	private void display() {
		for(Map.Entry<Integer,DoubleLinkedList> m:frequencyMap.entrySet())
		{
			System.out.println("Elements in bucket" + m.getKey());
			m.getValue().printNodes();
		}
	}
	
	class DoubleLinkedList {
		int listSize;
		DLLNode head;
		DLLNode tail;
		
		public DoubleLinkedList() {
			this.listSize=0;
			this.head = new DLLNode(0,0);
			this.tail = new DLLNode(0,0);
			head.next = tail;
			tail.prev = head;
		};
		
		public void addNode(DLLNode curNode) {
			DLLNode nextNode = head.next;
			curNode.next = nextNode;
			curNode.prev = head;
			head.next = curNode;
			nextNode.prev = curNode;
			listSize++;
		}
		
		public void removeNode(DLLNode curNode)	{
			DLLNode prevNode = curNode.prev;
			DLLNode nextNode = curNode.next;
			curNode.prev.next = curNode.next;
			curNode.next.prev = curNode.prev;
			listSize--;
		}
		
		public void printNodes()
		{
			DLLNode n1 = head.next;
			while(n1.next!=null)
			{
				System.out.println(n1.value);
				n1=n1.next;
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		LFUCache lfu = new LFUCache(3);
		lfu.put(1, 10);
		lfu.put(2, 20);
		lfu.put(3, 30);
		System.out.println("get of 1 is: "+lfu.get(1));
		lfu.put(4, 40);
		lfu.display();
	}

}
