package com.designptn.decorator;

//对功能进行扩展,makeClothes()方法不只是制作衣服，还绣花
public class EmbroideryClothesDecorator extends Decorator {

    public EmbroideryClothesDecorator(Clothes clothes) {
        super(clothes);
    }

    public void embroidery() {
        System.out.println("衣服绣花");
    }

    public void makeClothes() {
        super.makeClothes();
        this.embroidery();
    }
}