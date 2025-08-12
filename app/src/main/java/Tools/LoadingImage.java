package Tools;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;

public class LoadingImage {


    private View contentView;      // 内容视图
    private View loadingView;      // 加载视图
    private int animationDuration; // 动画持续时间

    /**
     * 构造函数
     * @param contentView 内容视图
     * @param loadingView 加载视图
     * @param context 上下文用于获取动画时间配置
     */
    public LoadingImage(View contentView, View loadingView, Context context) {
        this.contentView = contentView;
        this.loadingView = loadingView;
        // 获取系统默认的长动画时间
        this.animationDuration = context.getResources().getInteger(
                android.R.integer.config_longAnimTime);
    }
    // 添加加载视图
    public void addloadingView() {
        loadingView.setVisibility(View.VISIBLE);
    }
    // 初始化加载视图
    public void initView(){
        loadingView.setVisibility(View.GONE);
    }

    /**
     * 设置自定义动画时间
     * @param duration 动画持续时间(毫秒)
     */
    public void setAnimationDuration(int duration) {
        this.animationDuration = duration;
    }

    /**
     * 显示加载动画(内容淡入)
     */
    public void showLoading() {
        // 初始状态:内容不可见,加载视图可见
        contentView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);

        // 执行淡入动画
        crossfadeIn();
    }

    /**
     * 隐藏加载动画(内容淡出)
     */
    public void hideLoading() {
        // 执行淡出动画
        crossfadeOut();
    }

    /**
     * 内容淡入动画
     */
    private void crossfadeIn() {
        // 设置内容初始透明度为0但可见
        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);

        // 加载视图从1淡出到0
        loadingView.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingView.setVisibility(View.GONE);
                    }
                });

        // 内容从0淡入到1
        contentView.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);
    }

    /**
     * 内容淡出动画
     */
    private void crossfadeOut() {
        // 设置内容初始透明度为1且可见
        contentView.setAlpha(1f);
        contentView.setVisibility(View.VISIBLE);

        // 内容从1淡出到0
        contentView.animate()
                .alpha(0f)
                .setDuration(animationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        contentView.setVisibility(View.GONE);
                    }
                });
    }
}
