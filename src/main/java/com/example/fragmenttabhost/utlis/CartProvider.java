package com.example.fragmenttabhost.utlis;

import android.content.Context;
import android.util.SparseArray;

import com.example.fragmenttabhost.bean.ShoppingCart;
import com.example.fragmenttabhost.bean.Wares;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * 购物车数据处理类  添加至购物车 从购物车删除等操作
 */
public class CartProvider {



    public static final String CART_JSON="cart_json";

    private SparseArray<ShoppingCart> datas =null;


    private  Context mContext;


    public CartProvider(Context context){

        mContext = context;
       datas = new SparseArray<>(10);
        listToSparse();

    }


    //添加至购物车
    public void put(ShoppingCart cart){


       ShoppingCart temp =  datas.get(cart.getId().intValue());

        if(temp !=null){
            temp.setCount(temp.getCount()+1);
        }
        else{
            temp = cart;
            temp.setCount(1);
        }

        datas.put(cart.getId().intValue(),temp);

        commit();

    }

      //添加
    public void put(Wares wares){


        ShoppingCart cart = convertData(wares);
        put(cart);
    }

    //更新购物车
    public void update(ShoppingCart cart){

        datas.put(cart.getId().intValue(),cart);
        commit();
    }


    //从购物车中删除
    public void delete(ShoppingCart cart){
        datas.delete(cart.getId().intValue());
        commit();
    }

    public List<ShoppingCart> getAll(){

        return  getDataFromLocal();
    }


    public void commit(){


        List<ShoppingCart> carts = sparseToList();

        PreferencesUtils.putString(mContext,CART_JSON,JSONUtil.toJSON(carts));

    }


    private List<ShoppingCart> sparseToList(){


        int size = datas.size();

        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i=0;i<size;i++){

            list.add(datas.valueAt(i));
        }
        return list;

    }



    private void listToSparse(){

        List<ShoppingCart> carts =  getDataFromLocal();

        if(carts!=null && carts.size()>0){

            for (ShoppingCart cart:
                    carts) {

                datas.put(cart.getId().intValue(),cart);
            }
        }

    }



    public  List<ShoppingCart> getDataFromLocal(){

        String json = PreferencesUtils.getString(mContext,CART_JSON);
        List<ShoppingCart> carts =null;
        if(json !=null ){

            carts = JSONUtil.fromJson(json,new TypeToken<List<ShoppingCart>>(){}.getType());

        }

        return  carts;

    }


    public ShoppingCart convertData(Wares item){

        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }



}
