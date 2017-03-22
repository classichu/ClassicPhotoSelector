package com.classichu.photoselector.customselector.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.classichu.photoselector.R;
import com.classichu.photoselector.customselector.bean.ImagePickerDirBean;
import com.classichu.photoselector.listener.OnNotFastClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisgeek on 2016/11/3.
 */

public class ImagePickerDirListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private View mLastClickDirItemChildView = null;
    private List<ImagePickerDirBean> mImagePickerDirBeanList=new ArrayList<>();
    public ImagePickerDirListAdapter(List<ImagePickerDirBean> dataList) {
        mImagePickerDirBeanList=dataList;
    }





    public View getLastClickDirItemChildView() {
        return mLastClickDirItemChildView;
    }

    public void setLastClickDirItemChildView(View lastClickDirItemChildView) {
        mLastClickDirItemChildView = lastClickDirItemChildView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_image_picker_dir, parent, false);
        return new ImagePickerDirListRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ImagePickerDirListRecyclerViewHolder recyclerViewHolder = (ImagePickerDirListRecyclerViewHolder) holder;
        if (position == 0) {
            recyclerViewHolder.id_tv_item_image_more.setVisibility(View.VISIBLE);
            mLastClickDirItemChildView = recyclerViewHolder.id_tv_item_image_more;
        } else {
            recyclerViewHolder.id_tv_item_image_more.setVisibility(View.GONE);
        }

        recyclerViewHolder.id_tv_item_title.setText(mImagePickerDirBeanList.get(position).getDirName().replace("/", "")
                + "(" + mImagePickerDirBeanList.get(position).getImageCount() + "张)");

      //  KLog.d("getDirImageIconPath" + data.getDirImageIconPath());
        //GlideHelper.displayImage(id_tv_item_image, data.getDirImageIconPath());
        Glide.with(recyclerViewHolder.id_tv_item_image.getContext())
                .load(mImagePickerDirBeanList.get(position).getDirImageIconPath())
               //.placeholder(R.drawable.img_image_no)
              //  .error(R.drawable.img_image_no)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade().into(recyclerViewHolder.id_tv_item_image);
      //  ImageLoaderFactory.getManager().displayImage(id_tv_item_image, data.getDirImageIconPath());

        recyclerViewHolder.itemView.setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                    if (onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v,position,mImagePickerDirBeanList.get(position));
                    }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mImagePickerDirBeanList.size();
    }


    private class ImagePickerDirListRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView id_tv_item_title;
        private ImageView id_tv_item_image;
        private ImageView id_tv_item_image_more;

        public ImagePickerDirListRecyclerViewHolder(View itemView) {
            super(itemView);
            id_tv_item_title = (TextView) itemView.findViewById(R.id.id_tv_item_title);
            id_tv_item_image = (ImageView) itemView.findViewById(R.id.id_tv_item_image);
            id_tv_item_image_more = (ImageView) itemView.findViewById(R.id.id_tv_item_image_more);


        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    private  OnItemClickListener onItemClickListener;

    public  interface  OnItemClickListener{
        void  OnItemClick(View view, int position, ImagePickerDirBean imagePickerDirBean);
    }
}
