package t3.giftbook.util.flipviewpager.sample;


import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.gc.materialdesign.widgets.SnackBar;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.squareup.picasso.Picasso;

import t3.giftbook.util.FlipViewPager;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private FlipViewPager pager;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawer;
    static String LOGTAG = "";
     boolean isForwarding = true;
    int prevPageNum=0;
    static Uri uri;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        this.pager = (FlipViewPager)findViewById(R.id.flipviewpager);
        this.pager.setOnPageChangeListener(new FlipViewPager.SimpleOnPageChangeListener(){
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // This space for rent
                if(position==0) {
                }
            }
        });
        this.pager.setAdapter(new PagerAdapter() {
        	private final LayoutInflater inflater;
        	{
    	        this.inflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        	}
        	
			@Override public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override public int getCount() {
				return 60;
			}
			
			@Override public Object instantiateItem(ViewGroup container, int position) {

                if(position>prevPageNum){isForwarding=true;}else{isForwarding=false;}
				FrameLayout v = (FrameLayout) inflater.inflate(R.layout.activity_main_pager_item, null);
				TextView text  = (TextView )v.findViewById(R.id.text);
		        
				//final ImageView img = (ImageView)v.findViewById(R.id.imageView1);
				//Picasso.with(getBaseContext()).load("http://lohas.nicoseiga.jp//thumb/"+a.getId(0)+"i?").into(img);
				
				StringBuilder sb = new StringBuilder();
				for(int i=0;i<1;i++){
				sb.append("page  " + position);
				}
				text.setText(sb.toString());

		        AsyncHttpRequest task = new AsyncHttpRequest(getBaseContext(),v,position,MainActivity.this,isForwarding);
		        Uri.Builder builder = new Uri.Builder();
		        task.execute(builder);
				
				container.addView(v);
                prevPageNum=position;
				return v;
			}
			@Override public void destroyItem(ViewGroup container, int position, Object object) {
				//TODO 
			}


		});


        //------------------------------------------------------------
        ImageView mainicon = new ImageView(this); // Create an icon
        Resources res = getResources();
        Drawable drawable_clip = res.getDrawable(R.drawable.ic_action_attachment_2);
        Drawable drawable_add = res.getDrawable(R.drawable.ic_action_add);
        Drawable drawable_download = res.getDrawable(R.drawable.ic_action_download);
        Drawable drawable_globe = res.getDrawable(R.drawable.ic_action_globe);
        mainicon.setImageDrawable(drawable_add);//main circle

        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(mainicon)
                .build();
        //----------------------------------------------------------------
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
// repeat many times:
        ImageView itemIcon_clip = new ImageView(this);
        ImageView itemIcon_download = new ImageView(this);
        ImageView itemIcon_globe = new ImageView(this);
        itemIcon_clip.setImageDrawable(drawable_clip);
        itemIcon_download.setImageDrawable(drawable_download);
        itemIcon_globe.setImageDrawable(drawable_globe);
        SubActionButton button1 = itemBuilder.setContentView(itemIcon_clip).build();
        SubActionButton button2 = itemBuilder.setContentView(itemIcon_download).build();
        SubActionButton button3 = itemBuilder.setContentView(itemIcon_globe).build();

        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }
        });
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(button1)
                .addSubActionView(button2)
                .addSubActionView(button3)
                        // ...
                .attachTo(actionButton)
                .build();

        //------------------------------------------------------------------

        //-------------
        // ���C���R���e���c������View
       // TextView mainText = (TextView)findViewById(R.id.drawer_main_text);
        //mainText.setText("���C���R���e���c����");
 
        // NavigationDrawer�ɕ\������View
        TextView navigateText = (TextView)findViewById(R.id.drawer_text);
        navigateText.setText("navigation drawer");
 
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
 
        /**
         * ActionBarDrawerToggle�N���X���쐬
         * Activity,NavagationDrawer�̃��C�A�E�g,�O�{��̃A�C�R��,�J���̃A�N�V���������������ꂼ��w��
         */
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawer,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                //NavigationDrawer����������
            }
 
            @Override
            public void onDrawerOpened(View drawerView) {
                // Navigation Drawer���J��������
            }
 
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // �A�C�R���̃A�j���[�V�����̏���
                super.onDrawerSlide(drawerView, slideOffset);
            }
 
            @Override
            public void onDrawerStateChanged(int newState) {
                // NavigationDrawer�̏�Ԃ��ύX���ꂽ��
            }
        };
 
        // �쐬����ActionBarDrawerToggle��ݒu
        mDrawer.setDrawerListener(mDrawerToggle);
 
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
 
    }
 
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Activity�̊J�n�����������Ƃ��AActionBarDrawerToggle�̏�Ԃ𓯊���
        mDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Activity���ύX�������ɁAActionBarDrawerToggle���ύX������
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            // �{�^���������ꂽ���AActionBarDrawerToggle��NavigationDrawer�̊J������
            return true;
        }
 
        return super.onOptionsItemSelected(item);
    }
        //------------
}
        


