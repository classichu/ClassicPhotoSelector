package com.classichu.photoselector.customselector.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.classichu.photoselector.R;
import com.classichu.photoselector.customselector.bean.ImagePickerBean;
import com.classichu.photoselector.listener.OnNotFastClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisgeek on 2016/12/12.
 */

public class ImagePickerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE_CAMERA = 111;
    private boolean mShowCamera = true;

    public List<String> getSelectedImageList() {
        return mSelectedImageList;
    }

    private List<String> mSelectedImageList = new ArrayList<>();
    private List<ImagePickerBean> mImagePickerBeanList = new ArrayList<>();
    private int mMaxCount;

    public ImagePickerListAdapter(List<ImagePickerBean> dataList, int maxCount) {
        mImagePickerBeanList = dataList;
        mMaxCount = maxCount;
    }

    public void setShowCamera(boolean showCamera) {
        this.mShowCamera = showCamera;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_image_picker, parent, false);
        if (viewType == VIEW_TYPE_CAMERA) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_camera, parent, false);
            return new PhotoCameraListViewHolder(view);
        }
        return new ImagePickerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PhotoCameraListViewHolder) {
            holder.itemView.setOnClickListener(new OnNotFastClickListener() {
                @Override
                protected void onNotFastClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onPhotoCameraBtnClick();
                    }
                }
            });
            return;
        }
        final ImagePickerListViewHolder listViewHolder = (ImagePickerListViewHolder) holder;
        final int readPos = mShowCamera?position - 1:position;
        //GlideHelper.displayImage(id_iv_item_image, data.getPath());
        // ImageLoaderFactory.getManager().displayImage(id_iv_item_image, data.getPath());
        Glide.with(listViewHolder.id_iv_item_image.getContext())
                .load(mImagePickerBeanList.get(readPos).getPath())
                //.placeholder(R.drawable.img_image_no)
                //  .error(R.drawable.img_image_no)
               //### .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                //### .crossFade()
                .into(listViewHolder.id_iv_item_image);

        if (mSelectedImageList.contains(mImagePickerBeanList.get(readPos).getPath())) {
            listViewHolder.id_iv_selectbtn.setSelected(true);
            listViewHolder.itemView.setSelected(true);
        } else {
            listViewHolder.id_iv_selectbtn.setSelected(false);
            listViewHolder.itemView.setSelected(false);
        }

        listViewHolder.itemView.setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, readPos, mImagePickerBeanList.get(readPos));
                }
            }
        });

        listViewHolder.id_iv_selectbtn.setOnClickListener(new OnNotFastClickListener() {
            @Override
            protected void onNotFastClick(View v) {
                String path = mImagePickerBeanList.get(readPos).getPath();
                if (mSelectedImageList.contains(path)) {
                    mSelectedImageList.remove(path);
                    /**
                     * notifyDataSetChanged刷新会让图片闪烁   不采用adapter刷新
                     * 直接找在当前itemView上找R.id.id_iv_selectbtn 设置状态
                     */
                    // view.findViewById(R.id.id_iv_selectbtn).setSelected(false);
                    v.setSelected(false);
                    listViewHolder.itemView.setSelected(false);
                } else {
                    //
                    if (mSelectedImageList.size() >= mMaxCount) {
                        Toast.makeText(v.getContext(), "最多能选择" + mMaxCount + "张", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //
                    mSelectedImageList.add(path);
                    /**
                     * notifyDataSetChanged刷新会让图片闪烁   不采用adapter刷新
                     * 直接找在当前itemView上找R.id.id_iv_selectbtn 设置状态
                     */
                    //  view.findViewById(R.id.id_iv_selectbtn).setSelected(true);
                    v.setSelected(true);
                    listViewHolder.itemView.setSelected(true);
                }
                if (onItemClickListener != null) {
                    onItemClickListener.onItemSelected(mSelectedImageList.size());
                }

            }

        });
    }

    @Override
    public int getItemCount() {
        if (mShowCamera) {
            //不显示 Camera
            return mImagePickerBeanList.size() + 1;
        }
        return mImagePickerBeanList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mShowCamera) {
            return VIEW_TYPE_CAMERA;
        }
        return super.getItemViewType(position);
    }

    private class PhotoCameraListViewHolder extends RecyclerView.ViewHolder {

        public PhotoCameraListViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ImagePickerListViewHolder extends RecyclerView.ViewHolder {

        private ImageView id_iv_item_image;
        private ImageView id_iv_selectbtn;

        public ImagePickerListViewHolder(View itemView) {
            super(itemView);
            id_iv_item_image = (ImageView) itemView.findViewById(R.id.id_iv_item_image);
            id_iv_selectbtn = (ImageView) itemView.findViewById(R.id.id_iv_selectbtn);
        }
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public abstract static class OnItemClickListener {
        public void onItemClick(View view, int position, ImagePickerBean imagePickerBean) {
        }

        public void onItemSelected(int selectCount) {
        }

        public void onPhotoCameraBtnClick() {
        }
    }
}
