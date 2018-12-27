package com.barnettwong.quyou.ui.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.barnettwong.quyou.R;
import com.barnettwong.quyou.bean.book.BookInfoBean;
import com.barnettwong.quyou.bean.book.BookListData;
import com.barnettwong.quyou.bean.book.BookReviewBean;
import com.barnettwong.quyou.bean.book.BookReviewsData;
import com.barnettwong.quyou.ui.activity.BookMoreReviewActivity;
import com.barnettwong.quyou.ui.holder.BookSeriesCeilHolder;
import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.hymane.expandtextview.ExpandTextView;

import java.util.List;
import java.util.Random;

/**
 * Author   :hymanme
 * Email    :hymanme@163.com
 * Create at 2016/2/22 0022
 * Description:
 */
public class BookDetailAdapter extends RecyclerView.Adapter {
    private static final int TYPE_BOOK_INFO = 0;
    private static final int TYPE_BOOK_BRIEF = 1;
    private static final int TYPE_BOOK_COMMENT = 2;
    private static final int TYPE_BOOK_RECOMMEND = 3;

    public static final int HEADER_COUNT = 2;
    private static final int AVATAR_SIZE_DP = 24;
    private static final int ANIMATION_DURATION = 600;
    //模拟加载时间
    private static final int PROGRESS_DELAY_MIN_TIME = 500;
    private static final int PROGRESS_DELAY_SIZE_TIME = 1000;

    private Activity mContext;
    private final BookInfoBean mBookInfo;
    private final BookReviewsData mReviewsListResponse;
    private final BookListData mSeriesListResponse;

    //图书出版信息是否展开
    private boolean flag;

    public BookDetailAdapter(Activity mContext,BookInfoBean mBookInfo, BookReviewsData mReviewsListResponse, BookListData mSeriesListResponse) {
        this.mContext=mContext;
        this.mBookInfo = mBookInfo;
        this.mReviewsListResponse = mReviewsListResponse;
        this.mSeriesListResponse = mSeriesListResponse;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_BOOK_INFO) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_info, parent, false);
            return new BookInfoHolder(view);
        } else if (viewType == TYPE_BOOK_BRIEF) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_brief, parent, false);
            return new BookBriefHolder(view);
        } else if (viewType == TYPE_BOOK_COMMENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_comment, parent, false);
            return new BookCommentHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_series, parent, false);
            return new BookSeriesHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BookInfoHolder) {
            ((BookInfoHolder) holder).ratingBar_hots.setRating(Float.valueOf(mBookInfo.getRating().getAverage()) / 2);
            ((BookInfoHolder) holder).tv_hots_num.setText(mBookInfo.getRating().getAverage());
            ((BookInfoHolder) holder).tv_comment_num.setText(mBookInfo.getRating().getNumRaters() + mContext.getString(R.string.comment_num));
            ((BookInfoHolder) holder).tv_book_info.setText(mBookInfo.getInfoString());
            //详细信息
            StringBuilder sb = new StringBuilder();
            if (mBookInfo.getAuthor().length > 0) {
                sb.append("作者:").append(mBookInfo.getAuthor()[0]).append("\n");
            }
            sb.append("出版社:").append(mBookInfo.getPublisher()).append("\n");
            if (mBookInfo.getSubtitle().isEmpty()) {
                ((BookInfoHolder) holder).tv_subtitle.setVisibility(View.GONE);
            }
            sb.append("副标题:").append(mBookInfo.getSubtitle()).append("\n");
            if (mBookInfo.getOrigin_title().isEmpty()) {
                ((BookInfoHolder) holder).tv_origin_title.setVisibility(View.GONE);
            }
            sb.append("原作名:").append(mBookInfo.getOrigin_title()).append("\n");
            if (mBookInfo.getTranslator().length > 0) {
                sb.append("译者:").append(mBookInfo.getTranslator()[0]).append("\n");
            } else {
                ((BookInfoHolder) holder).tv_translator.setVisibility(View.GONE);
            }
            sb.append("出版年:").append(mBookInfo.getPubdate()).append("\n");
            sb.append("页数:").append(mBookInfo.getPages()).append("\n");
            sb.append("定价:").append(mBookInfo.getPrice()).append("\n");
            sb.append("装帧:").append(mBookInfo.getBinding()).append("\n");
            sb.append("isbn:").append(mBookInfo.getIsbn13()).append("\n");

            ((BookInfoHolder) holder).rl_more_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag) {
                        ObjectAnimator.ofFloat(((BookInfoHolder) holder).iv_more_info, "rotation", 90, 0).start();
                        flag = false;
                    } else {
                        ObjectAnimator.ofFloat(((BookInfoHolder) holder).iv_more_info, "rotation", 0, 90).start();
                        new AlertDialog.Builder(mContext)
                                .setTitle("详细信息：")
                                .setMessage(sb)
                                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        ObjectAnimator.ofFloat(((BookInfoHolder) holder).iv_more_info, "rotation", 90, 0).start();
                                        flag = false;
                                    }
                                })
                                .create().show();
                        flag = true;
                    }
                }
            });
        } else if (holder instanceof BookBriefHolder) {
            if (!mBookInfo.getSummary().isEmpty()) {
                ((BookBriefHolder) holder).etv_brief.setContent(mBookInfo.getSummary());
            } else {
                ((BookBriefHolder) holder).etv_brief.setContent(mContext.getString(R.string.no_brief));
            }
        } else if (holder instanceof BookCommentHolder) {
            List<BookReviewBean> reviews = mReviewsListResponse.getReviews();
            if (reviews.isEmpty()) {
                ((BookCommentHolder) holder).itemView.setVisibility(View.GONE);
            } else if (position == HEADER_COUNT) {
                ((BookCommentHolder) holder).tv_comment_title.setVisibility(View.VISIBLE);
            } else if (position == reviews.size() + 1) {
                ((BookCommentHolder) holder).tv_more_comment.setVisibility(View.VISIBLE);
                ((BookCommentHolder) holder).tv_more_comment.setText(mContext.getString(R.string.more_brief) + mReviewsListResponse.getTotal() + "条");
                ((BookCommentHolder) holder).tv_more_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, BookMoreReviewActivity.class);
                        intent.putExtra("bookId", mBookInfo.getId());
                        intent.putExtra("bookName", mBookInfo.getTitle());
                        mContext.startActivity(intent);
                    }
                });
            }
            Glide.with(mContext)
                    .load(reviews.get(position - HEADER_COUNT).getAuthor().getAvatar())
                    .asBitmap()
                    .centerCrop()
                    .into(new BitmapImageViewTarget(((BookCommentHolder) holder).iv_avatar) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ((BookCommentHolder) holder).iv_avatar.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            ((BookCommentHolder) holder).tv_user_name.setText(reviews.get(position - HEADER_COUNT).getAuthor().getName());
            if (reviews.get(position - HEADER_COUNT).getRating() != null) {
                ((BookCommentHolder) holder).ratingBar_hots.setRating(Float.valueOf(reviews.get(position - HEADER_COUNT).getRating().getValue()));
            }
            ((BookCommentHolder) holder).tv_comment_content.setText(reviews.get(position - HEADER_COUNT).getSummary());
            ((BookCommentHolder) holder).tv_favorite_num.setText(reviews.get(position - HEADER_COUNT).getVotes() + "");
            ((BookCommentHolder) holder).tv_update_time.setText(reviews.get(position - HEADER_COUNT).getUpdated().split(" ")[0]);
        } else if (holder instanceof BookSeriesHolder) {
            final List<BookInfoBean> seriesBooks = mSeriesListResponse.getBooks();
            if (seriesBooks.isEmpty()) {
                ((BookSeriesHolder) holder).itemView.setVisibility(View.GONE);
            } else {
                BookSeriesCeilHolder ceilHolder;
                ((BookSeriesHolder) holder).ll_series_content.removeAllViews();
                for (int i = 0; i < seriesBooks.size(); i++) {
                    ceilHolder = new BookSeriesCeilHolder(mContext,seriesBooks.get(i));
                    ((BookSeriesHolder) holder).ll_series_content.addView(ceilHolder.getContentView());
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BOOK_INFO;
        } else if (position == 1) {
            return TYPE_BOOK_BRIEF;
        } else if (position > 1 && position < (mReviewsListResponse == null ? HEADER_COUNT : (mReviewsListResponse.getReviews().size() + HEADER_COUNT))) {
            return TYPE_BOOK_COMMENT;
        } else {
            return TYPE_BOOK_RECOMMEND;
        }
    }

    @Override
    public int getItemCount() {
        int count = HEADER_COUNT;
        if (mReviewsListResponse != null) {
            if(!ObjectUtils.isEmpty(mReviewsListResponse.getReviews())){
                count += mReviewsListResponse.getReviews().size();
            }
        }
        if (mSeriesListResponse != null ) {
            if(!ObjectUtils.isEmpty(mSeriesListResponse.getBooks())){
                count += 1;
            }
        }
        return count;
    }

    class BookInfoHolder extends RecyclerView.ViewHolder {
        private AppCompatRatingBar ratingBar_hots;
        private TextView tv_hots_num;
        private TextView tv_comment_num;
        private TextView tv_book_info;
        private ImageView iv_more_info;
        private ProgressBar progressBar;
        private RelativeLayout rl_more_info;
        private LinearLayout ll_publish_info;

        private TextView tv_author;
        private TextView tv_publisher;
        private TextView tv_subtitle;
        private TextView tv_origin_title;
        private TextView tv_translator;
        private TextView tv_publish_date;
        private TextView tv_pages;
        private TextView tv_price;
        private TextView tv_binding;
        private TextView tv_isbn;


        public BookInfoHolder(View itemView) {
            super(itemView);
            ratingBar_hots = (AppCompatRatingBar) itemView.findViewById(R.id.ratingBar_hots);
            tv_hots_num = (TextView) itemView.findViewById(R.id.tv_hots_num);
            tv_comment_num = (TextView) itemView.findViewById(R.id.tv_comment_num);
            tv_book_info = (TextView) itemView.findViewById(R.id.tv_book_info);
            iv_more_info = (ImageView) itemView.findViewById(R.id.iv_more_info);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            rl_more_info = (RelativeLayout) itemView.findViewById(R.id.rl_more_info);
            ll_publish_info = (LinearLayout) itemView.findViewById(R.id.ll_publish_info);

            tv_author = (TextView) itemView.findViewById(R.id.tv_author);
            tv_publisher = (TextView) itemView.findViewById(R.id.tv_publisher);
            tv_subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            tv_origin_title = (TextView) itemView.findViewById(R.id.tv_origin_title);
            tv_translator = (TextView) itemView.findViewById(R.id.tv_translator);
            tv_publish_date = (TextView) itemView.findViewById(R.id.tv_publish_date);
            tv_pages = (TextView) itemView.findViewById(R.id.tv_pages);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_binding = (TextView) itemView.findViewById(R.id.tv_binding);
            tv_isbn = (TextView) itemView.findViewById(R.id.tv_isbn);
        }
    }

    class BookBriefHolder extends RecyclerView.ViewHolder {
        private ExpandTextView etv_brief;

        public BookBriefHolder(View itemView) {
            super(itemView);
            etv_brief = (ExpandTextView) itemView.findViewById(R.id.etv_brief);
        }
    }

    class BookCommentHolder extends RecyclerView.ViewHolder {
        private TextView tv_comment_title;
        private ImageView iv_avatar;
        private TextView tv_user_name;
        private AppCompatRatingBar ratingBar_hots;
        private TextView tv_comment_content;
        private ImageView iv_favorite;
        private TextView tv_favorite_num;
        private TextView tv_update_time;
        private TextView tv_more_comment;

        public BookCommentHolder(View itemView) {
            super(itemView);
            tv_comment_title = (TextView) itemView.findViewById(R.id.tv_comment_title);
            iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            ratingBar_hots = (AppCompatRatingBar) itemView.findViewById(R.id.ratingBar_hots);
            tv_comment_content = (TextView) itemView.findViewById(R.id.tv_comment_content);
            iv_favorite = (ImageView) itemView.findViewById(R.id.iv_favorite);
            tv_favorite_num = (TextView) itemView.findViewById(R.id.tv_favorite_num);
            tv_update_time = (TextView) itemView.findViewById(R.id.tv_update_time);
            tv_more_comment = (TextView) itemView.findViewById(R.id.tv_more_comment);
        }
    }

    class BookSeriesHolder extends RecyclerView.ViewHolder {
        private HorizontalScrollView hsv_series;
        private LinearLayout ll_series_content;

        public BookSeriesHolder(View itemView) {
            super(itemView);
            hsv_series = (HorizontalScrollView) itemView.findViewById(R.id.hsv_series);
            ll_series_content = (LinearLayout) itemView.findViewById(R.id.ll_series_content);
        }
    }

    private int getDelayTime() {
        return new Random().nextInt(PROGRESS_DELAY_SIZE_TIME) + PROGRESS_DELAY_MIN_TIME;
    }
}
