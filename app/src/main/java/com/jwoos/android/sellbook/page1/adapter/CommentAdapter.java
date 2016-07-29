package com.jwoos.android.sellbook.page1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.jwoos.android.sellbook.R;
import com.jwoos.android.sellbook.base.Gloval;
import com.jwoos.android.sellbook.base.retrofit.ServiceGenerator;
import com.jwoos.android.sellbook.base.retrofit.model.Comment;
import com.jwoos.android.sellbook.utils.ObjectUtils;
import com.jwoos.android.sellbook.utils.TimeFomat;
import com.jwoos.android.sellbook.widget.MLRoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> comment;
    private Activity mContext;
    private String base_image_url_profile;
    private MaterialDialog mMaterialDialog;
    private String book_id;

    public CommentAdapter(Context context, List<Comment> objects, String book_id) {
        this.mContext = (Activity) context;
        this.comment = objects;
        this.book_id = book_id;
        this.base_image_url_profile = Gloval.getBase_image_url_profile();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_comment, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        try {
            viewHolder.bind(i);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return comment.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @BindView(R.id.swipe_view)
        SwipeLayout sample2;
        @Nullable
        @BindView(R.id.card_profile)
        MLRoundedImageView iv_profile;
        @Nullable
        @BindView(R.id.user_nic)
        TextView tv_nic;
        @Nullable
        @BindView(R.id.time)
        TextView tv_time;
        @Nullable
        @BindView(R.id.content)
        TextView tv_content;
        @Nullable
        @BindView(R.id.delete)
        ImageView btn_delete;
        @Nullable
        @BindView(R.id.edit)
        ImageView btn_edit;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void bind(int position) throws ParseException {

            //프로필 사진 회전값
            int profile_rotate = Character.getNumericValue(comment.get(position).getUser_profile().charAt(9));
            Picasso.with(mContext)
                    .load(base_image_url_profile + comment.get(position).getUser_profile())
                    .fit()
                    .rotate(orientation(profile_rotate))
                    .into(iv_profile);

            tv_nic.setText(comment.get(position).getUser_nik());
            tv_content.setText(comment.get(position).getCmt_content());
            tv_time.setText(TimeFomat.comment_time(comment.get(position).getCmt_time()));

            if (comment.get(position).getUser_chk().equals("OK")) {
                sample2.setShowMode(SwipeLayout.ShowMode.LayDown);
                sample2.addDrag(SwipeLayout.DragEdge.Right, sample2.findViewWithTag("Bottom2"));
                Log.d("스와이프", String.valueOf(sample2.getOpenStatus()));

                click(position);
            } else {
                sample2.setSwipeEnabled(false);
            }
        }

        private void click(final int position) {
            assert btn_delete != null;
            btn_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sample2.close();
                    commentRemove(position);

                }
            });

            assert btn_edit != null;
            btn_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sample2.close();
                    showEditDialog(position);
                }
            });
        }

    }

    private void showEditDialog(final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View promptView = layoutInflater.inflate(R.layout.layout_dialog_custom3, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.comment_edit);
        editText.setText(comment.get(position).getCmt_content());
        editText.setSelection(editText.getText().length());
        alertDialogBuilder.setTitle("댓글수정");
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {
                        if (!ObjectUtils.isEmpty(editText.getText().toString())) {
                            ServiceGenerator.getService()
                                    .set_comment("", editText.getText().toString(), comment.get(position).getComment_id(), new Callback<Void>() {
                                        @Override
                                        public void success(Void aVoid, Response response) {
                                            dialog.cancel();
                                            commentSetting();
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {

                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void commentRemove(final int position) {
        mMaterialDialog = new MaterialDialog(mContext)
                .setMessage("이 댓글을 삭제하시겟어요?")
                .setPositiveButton("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ServiceGenerator.getService()
                                .delete_comment(comment.get(position).getComment_id(), new Callback<Void>() {
                                    @Override
                                    public void success(Void aVoid, Response response) {
                                        mMaterialDialog.dismiss();
                                        commentSetting();
                                        Toast.makeText(mContext,"댓글이 삭제되었습니다",Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {

                                    }
                                });
                    }
                })
                .setNegativeButton("취소", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    public void commentSetting() {
        ServiceGenerator.getService().get_comment(book_id, new Callback<List<Comment>>() {
            @Override
            public void success(List<Comment> comments, Response response) {
                comment.clear();
                comment = comments;
                notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private int orientation(int orientation) {

        int rotate = 0;

        if (orientation == 2)
            rotate = 0;
        else if (orientation == 3)
            rotate = 180;
        else if (orientation == 4)
            rotate = 180;
        else if (orientation == 5)
            rotate = 90;
        else if (orientation == 6)
            rotate = 90;
        else if (orientation == 7)
            rotate = -90;
        else if (orientation == 8)
            rotate = -90;

        return rotate;
    }

}
