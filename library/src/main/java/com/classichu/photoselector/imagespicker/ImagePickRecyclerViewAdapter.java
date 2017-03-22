package com.classichu.photoselector.imagespicker;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.classichu.photoselector.R;
import com.classichu.photoselector.listener.OnNotFastClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisgeek on 2016/7/13.
 */
public class ImagePickRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<ImagePickBean> mImagePickBeanList=new ArrayList<>();
    final  int TYPE_NORMAL=0;
    final  int TYPE_ADD=1;

    final  int  PICK_IMAGE_MAX_COUNT_DEFAULT=3;//default

    int OneColumnImageCount;

    int mPickImageMaxCount=PICK_IMAGE_MAX_COUNT_DEFAULT;

    boolean mIsCanDelete=true;
    public void setPickImageMaxCount(int pickImageMaxCount) {
        mPickImageMaxCount = pickImageMaxCount;
    }
    public void setIsCanDelete(boolean isCanDelete) {
        mIsCanDelete = isCanDelete;
    }


    public ImagePickRecyclerViewAdapter(List<ImagePickBean> imagePickBeanList, int mOneColumnImageCount) {
        mImagePickBeanList =imagePickBeanList;
        OneColumnImageCount=mOneColumnImageCount;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder=null;
        switch (viewType) {
            case TYPE_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_show_img,parent,false);
                viewHolder =new MyNormalRecyclerViewViewHolder(view);
                break;
            case TYPE_ADD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_add_btn,parent,false);
                viewHolder =new MyAddRecyclerViewViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyAddRecyclerViewViewHolder) {
            MyAddRecyclerViewViewHolder myAddRecyclerViewViewHolder=(MyAddRecyclerViewViewHolder)holder;
            //设置宽高
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) myAddRecyclerViewViewHolder.imageViewAdd.getLayoutParams();
            int imageViewWidth = dealImageViewW(myAddRecyclerViewViewHolder.imageViewAdd.getContext());
            //lp.width = SizeTool.dp2px(myAddRecyclerViewViewHolder.imageView.getContext(),85);
            lp.width = imageViewWidth;
            //lp.height = SizeTool.dp2px(myAddRecyclerViewViewHolder.imageView.getContext(),85);
            lp.height =imageViewWidth;
            myAddRecyclerViewViewHolder.imageViewAdd.setLayoutParams(lp);

            myAddRecyclerViewViewHolder.imageViewAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onAddClickInnerListener.onAddInnerClick(v,mPickImageMaxCount,mImagePickBeanList.size());
                }
            });


        } else if (holder instanceof MyNormalRecyclerViewViewHolder) {
            final MyNormalRecyclerViewViewHolder myNormalRecyclerViewViewHolder=(MyNormalRecyclerViewViewHolder)holder;

            //设置宽高
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) myNormalRecyclerViewViewHolder.imageView.getLayoutParams();
            int imageViewWidth = dealImageViewW(myNormalRecyclerViewViewHolder.imageView.getContext());
            //lp.width = SizeTool.dp2px(myNormalRecyclerViewViewHolder.imageView.getContext(),85);
            lp.width = imageViewWidth;
            //lp.height = SizeTool.dp2px(myNormalRecyclerViewViewHolder.imageView.getContext(),85);
            lp.height =imageViewWidth;
            myNormalRecyclerViewViewHolder.imageView.setLayoutParams(lp);


            String imagePathOrUrl=mImagePickBeanList.get(position).getImagePathOrUrl();
            Log.d("上传", "onBindViewHolder: imagePathOrUrl:"+imagePathOrUrl);
            imagePathOrUrl = imagePathOrUrl.toLowerCase().matches("http.+") ? imagePathOrUrl : "file:///" + imagePathOrUrl;
            Glide.with(myNormalRecyclerViewViewHolder.imageView.getContext())
                    .load(imagePathOrUrl)
                    .centerCrop()
                  //  .error(R.drawable.logo)
                    .into(myNormalRecyclerViewViewHolder.imageView);

            String imageWebIdStr=mImagePickBeanList.get(position).getImageWebIdStr();
            Log.d("上传", "onBindViewHolder: imageWebIdStr:"+imageWebIdStr);
           /* myNormalRecyclerViewViewHolder.id_tlv_pic.setVisibility(View.VISIBLE);
            if (imageWebIdStr==null||imageWebIdStr.equals("")){
                myNormalRecyclerViewViewHolder.id_tlv_pic.setText("十");
                myNormalRecyclerViewViewHolder.id_tlv_pic.setBgColor(Color.parseColor("#E10602"));
            }else{

                myNormalRecyclerViewViewHolder.id_tlv_pic.setBgColor(Color.parseColor("#5BB260"));
                myNormalRecyclerViewViewHolder.id_tlv_pic.setText("√");
            }*/
            if (mIsCanDelete){
                myNormalRecyclerViewViewHolder.btn_delete.setVisibility(View.VISIBLE);
                myNormalRecyclerViewViewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onDeleteInnerClickListener.onDeleteInnerClick(v,position);
                    }
                });
            }else{
                myNormalRecyclerViewViewHolder.btn_delete.setVisibility(View.GONE);
            }

            myNormalRecyclerViewViewHolder.itemView.setOnClickListener(new OnNotFastClickListener() {
                @Override
                protected void onNotFastClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.onItemClick(v,position);
                    }
                }
            });
        }
    }

    /**
     * 返回px
     * @param context
     * @return
     */
    private int dealImageViewW(Context context) {
        int rongQi_padding_px=dip2px(10+10);//left padding 10dp right 10dp
        int rongQi_margin_px=dip2px(2+2);//left margin  2dp  right 2dp

        //int screen_w= ScreenUtil.getScreenWidth(context);//720px
        int screen_w= Resources.getSystem().getDisplayMetrics().widthPixels;//720px
        int screen_w_without_rongQi= screen_w-rongQi_padding_px-rongQi_margin_px;
        return screen_w_without_rongQi/OneColumnImageCount-dip2px(4+4);//自身的margin left margin  4dp  right 4dp
    }

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (mImagePickBeanList.size()<mPickImageMaxCount&&position==mImagePickBeanList.size()+1-1){
            return TYPE_ADD;
        }else{
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mImagePickBeanList.size()<mPickImageMaxCount?mImagePickBeanList.size()+1:mPickImageMaxCount;//add
    }


   class MyNormalRecyclerViewViewHolder  extends RecyclerView.ViewHolder{

       ImageView imageView;
       Button btn_delete;
      // TagLabelView id_tlv_pic;
       public MyNormalRecyclerViewViewHolder(View itemView) {
           super(itemView);
           imageView= (ImageView) itemView.findViewById(R.id.imageView);
           btn_delete= (Button) itemView.findViewById(R.id.btn_delete);
          // id_tlv_pic= (TagLabelView) itemView.findViewById(R.id.id_tlv_pic);
       }
   }
    class MyAddRecyclerViewViewHolder  extends RecyclerView.ViewHolder{

        ImageView imageViewAdd;
        public MyAddRecyclerViewViewHolder(View itemView) {
            super(itemView);
            //ADD
            imageViewAdd = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }


    public  interface OnAddClickInnerListener{
        void onAddInnerClick(View view, int pickImageMaxCount, int hasImageCount);
    }
    public void setOnAddClickInnerListener(OnAddClickInnerListener onAddClickInnerListener) {
        this.onAddClickInnerListener = onAddClickInnerListener;
    }
    OnAddClickInnerListener onAddClickInnerListener;


    public  interface OnDeleteInnerClickListener{
        void onDeleteInnerClick(View view, int pos);
    }

    public void setOnDeleteInnerClickListener(OnDeleteInnerClickListener onDeleteInnerClickListener) {
        this.onDeleteInnerClickListener = onDeleteInnerClickListener;
    }

    OnDeleteInnerClickListener onDeleteInnerClickListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    private  OnItemClickListener onItemClickListener;

    public  interface  OnItemClickListener{
        void  onItemClick(View view, int position);
    }
    private int dip2px(float dp){
        float value = TypedValue.applyDimension(1, dp, Resources.getSystem().getDisplayMetrics());
        return (int)value;
    }
}
