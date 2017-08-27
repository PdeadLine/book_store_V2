package com.github.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车类
 * @author Administrator
 *
 */
public class Cart {
	private Map<String ,CartItem> map=  new LinkedHashMap<String,CartItem>();
	
	
	/**
	 * 计算合计！
	 * @return
	 */
	public double getTotal(){
		//合计等于所有小计之和
		BigDecimal total=new BigDecimal("0");
		for(CartItem cartItem : map.values()){
			BigDecimal subtotal=new BigDecimal(cartItem.getSubtotal()+"");
			total=total.add(subtotal);
		}
		return total.doubleValue();
	}
	
	
	/**
	 * 添加条目到购物车中
	 * @param cartItem
	 */
	public void add(CartItem cartItem){
		if(map.containsKey(cartItem.getBook().getBid())){//判断车中原来是否存在该条目
			CartItem _cartItem=map.get(cartItem.getBook().getBid());
			_cartItem.setCount(_cartItem.getCount()+cartItem.getCount());
			map.put(cartItem.getBook().getBid(), _cartItem);
		}else{
			map.put(cartItem.getBook().getBid(), cartItem);
		}
	}
	/**
	 * 清空购物车条目
	 */
	public void clear(){
		map.clear();
	}
	/**
	 * 删除指定条目
	 * @param bid
	 */
	public void delete(String bid){
		map.remove(bid);
	}
	
	/**
	 * 我的购物车（获取所有条目）
	 * @return
	 */
	public Collection<CartItem> getCartItems(){
		return map.values();
	}
	

}
