import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class GreedyWarehouse 
{
  	private Map<String, Product> map =  new HashMap<String, Product>();
	public ReentrantLock l= new ReentrantLock();
   
	private class Product
	{
		Condition cond= new Condition(l);
		int quantity = 0;
	}

	private Product get(String item) 
	{
		Product p = map.get(item);
		if (p != null) 
			return p;
		p = new Product();
		map.put(item, p);
		return p;
	}

	public void supply(String item, int quantity)
	{
		Product p = get(item);
		p.quantity += quantity;
	}

	// Errado se faltar algum produto...
	public void consume(Set<String> items)
	{
		List<Product> done= new ArrayList<Product>();

		l.lock();
		try
		{
			for (Stirng s: items)
			{
				Product p= get(s);
				if (p.quantity== 0)
				{
					l.unlock();
					consume(items);
				}
				else
					done.add(p);
			}
			for (Product p: done)
			{
				p.quantity--;
			}
		}
		finally
		{
			l.unlock();
		}
	}
}
