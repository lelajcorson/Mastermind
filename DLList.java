public class DLList<E> implements Serializeable{
    private Node<E> head;
    private Node<E> tail;
    private int size;
    private Node<E> tracker;

    public DLList(){
        size = 0;
        head = new Node<E>(null);
        tail = new Node<E>(null);
        head.setNext(tail);
        head.setPrev(null);
        tail.setNext(null);
        tail.setPrev(head);

        tracker = null;
    }

    public void add(E data){
        Node<E> before = tail.prev();
        Node<E> after = tail;
        Node<E> newNode = new Node<E>(data);

        before.setNext(newNode);
        after.setPrev(newNode);
        newNode.setNext(after);
        newNode.setPrev(before);

        size ++;
    }

    public E next(){
        if(tracker == null){
            tracker = head.next();
            return tracker.get();
        }
        
        tracker = tracker.next();
        return tracker.get();
    }

    public void reset(){
        tracker = null;
    }

    public E get(int loc){
        Node<E> current = head.next();
        for(int i = 0; i < loc; i ++){
            current = current.next();
        }

        return current.get();
    }

    private Node<E> getNode(int location){
		Node<E> current = head.next();
		for(int i=0; i<location; i++){
			current = current.next();
		}
		return current;
    }

    public void set(int index, E replacement) {
        getNode(index).set(replacement);
    } 

    public String toString(){
        String str = "";
        Node<E> current = head.next();
        for(int i = 0; i < size; i ++){
            str += current.get() + "\n";
            current = current.next();
        }

        return str;
    }

    public void remove(E d){
        Node<E> current = head.next();
        for(int i = 0; i < size; i ++){
            if(current.get().equals(d)){
                Node<E> before = current.prev();
                Node<E> after = current.next();

                before.setNext(after);
                after.setPrev(before);
                size --;
                break;
            }
            current = current.next();
        }
    }

    public void remove(int loc){
        /*Node<E> current = head.next();
        for(int i = 0; i <= loc; i ++){
            if(i == loc){
                Node<E> before = current.prev();
                Node<E> after = current.next();

                before.setNext(after);
                after.setPrev(before);
                size --;
                System.out.println("size minused");
            }
            current = current.next();
        }*/

        Node<E> removeNode = getNode(loc);
        Node<E> before = removeNode.prev();
        Node<E> after = removeNode.next();

        before.setNext(after);
        after.setPrev(before);
        size --;
    }

    public int size(){
        return size;
    }
}