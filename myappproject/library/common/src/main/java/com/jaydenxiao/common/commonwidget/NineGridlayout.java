package com.jaydenxiao.common.commonwidget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jaydenxiao.common.commonutils.CommonUtil;
import com.jaydenxiao.common.commonutils.ScreenTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 2018/2/7.
 */

public class NineGridlayout extends ViewGroup {

    /**
     * 图片之间的间隔
     */
    private int gap = 10;
    private int columns;//
    private int rows;//
    private List<Image> listData;
    private List<Image> picData;
    private int totalWidth;

    public NineGridlayout(Context context) {
        super(context);
    }

    public NineGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ScreenTools screenTools = ScreenTools.instance(getContext());
        totalWidth = screenTools.getScreenWidth() - screenTools.dip2px(80);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    /**
     *
     *
     * @param width
     */
    public  void setTotalWidth(int width)
    {
        this.totalWidth=width;
    }

    private void layoutChildrenView() {
        int childrenCount = listData.size();

        int singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        int singleHeight = ScreenTools.instance(getContext()).dip2px(75);//singleWidth;

        //根据子view数量确定高度
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = singleHeight * rows + gap * (rows - 1);
        setLayoutParams(params);
        final List<String> pic=new ArrayList<>();
        for(Image m:picData)
        {
            pic.add(m.getUrl());
        }

        for (int i = 0; i < childrenCount; i++) {
            final int p=i;
            CustomImageView childrenView = (CustomImageView) getChildAt(i);
            childrenView.setImageUrl(((Image) listData.get(i)).getUrl());
         //   childrenView.setTag(i);
            childrenView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        //int pos = Integer.valueOf(v.getTag().toString());
                        if(iPhotoCallBack!=null)
                        {
                            iPhotoCallBack.photoCallBack(p, CommonUtil.join(",", pic.toArray(new String[0])));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            int[] position = findPosition(i);
            int left = (singleWidth + gap) * position[1];
            int top = (singleHeight + gap) * position[0];
            int right = left + singleWidth;
            int bottom = top + singleHeight;

            childrenView.layout(left, top, right, bottom);
        }

    }


    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }


    public void setImagesData(List<Image> lists,List<Image> picData) {
        if (lists == null || lists.isEmpty()) {
            return;
        }
        //初始化布局
        generateChildrenLayout(lists.size());
        //这里做一个重用view的处理
        if (listData == null) {
            int i = 0;
            while (i < lists.size()) {
                CustomImageView iv = generateImageView();
                addView(iv, generateDefaultLayoutParams());
                i++;
            }
        } else {
            int oldViewCount = listData.size();
            int newViewCount = lists.size();
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount - 1, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = 0; i < newViewCount - oldViewCount; i++) {
                    CustomImageView iv = generateImageView();
                    addView(iv, generateDefaultLayoutParams());
                }
            }
        }
        listData = lists;
        this.picData=picData;
        layoutChildrenView();
    }


    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * num	row	column
     * 1	   1	1
     * 2	   1	2
     * 3	   1	3
     * 4	   2	2
     * 5	   2	3
     * 6	   2	3
     * 7	   3	3
     * 8	   3	3
     * 9	   3	3
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= 3) {
            rows = 1;
            columns = length;
        } else if (length <= 6) {
            rows = 2;
            columns = 3;
            if (length == 4) {
                columns = 2;
            }
        } else {
            rows = 3;
            columns = 3;
        }
    }

    private CustomImageView generateImageView() {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        /*
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        */
        return iv;
    }

    public   interface  IPhotoCallBack{
         void photoCallBack(int pos,String url);
    }


    public IPhotoCallBack iPhotoCallBack;

    public  void setiPhotoCallBack(IPhotoCallBack iPhotoCallBack)
    {
        this.iPhotoCallBack=iPhotoCallBack;
    }


}