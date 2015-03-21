package t3.giftbook.util.flipviewpager.sample;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import t3.giftbook.util.FlipViewPager;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.gc.materialdesign.widgets.SnackBar;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class AsyncHttpRequest extends AsyncTask<Uri.Builder, Void, String> {
    
    private Activity mainActivity;
	static String[] imageId = new String[60];
	static String[] name = new String[60];
    static String[] title = new String[60];
	private Context mContext;
	private View rootView;
    private FlipViewPager pager;
    private int page;
    static boolean isFirstLoad = true;
    SnackBar snackbar;
    boolean isf;
	
    public String getId(int i) {
    	return imageId[i];
    }

    public AsyncHttpRequest(Context context,View rootView, int position, Activity mainactivity, boolean isForwarding) {
        this.mainActivity = mainactivity;
	    this.mContext=context;
	    this.rootView=rootView;
	    this.page = position;
        this.isf=isForwarding;
    }

    @Override
    protected String doInBackground(Uri.Builder... builder) {
    	if(isFirstLoad){
            isFirstLoad=false;
            Uri.Builder builder1 = new Uri.Builder();
        builder1.scheme("http");
        builder1.encodedAuthority("seiga.nicovideo.jp");
        builder1.path("/illust/ranking");
        //builder1.appendQueryParameter("track", "global_navi_top");

        HttpGet request = new HttpGet(builder1.build().toString());

        DefaultHttpClient httpClient = new DefaultHttpClient();

        try {
            String result = httpClient.execute(request, new ResponseHandler<String>() {
                @Override
                public String handleResponse(HttpResponse response)
                        throws ClientProtocolException, IOException {

                    switch (response.getStatusLine().getStatusCode()) {
                    case HttpStatus.SC_OK:
                        return EntityUtils.toString(response.getEntity(), "UTF-8");
                    
                    case HttpStatus.SC_NOT_FOUND:
                        throw new RuntimeException("SC_NOTFOUND"); //FIXME
                    
                    default:
                        throw new RuntimeException("DEFAULT"); //FIXME
                    }

                }
            });
            // logcat�Ƀ��X�|���X��\��
            int shift=0;
            String idx="ranking\" ><img src=\"http://lohas.nicoseiga.jp/thumb/";
            String namex="<span class=\"rank_txt_user\">";
            String titlex="<p class=\"rank_txt_title\">";
            for(int i=0;i<60;i++){
                imageId[i] = result.substring((result.indexOf(idx,shift)+idx.length()),(result.indexOf(idx,shift)+idx.length())+7);

                shift = result.indexOf(idx,shift)+idx.length()+7;
                int namehead = result.indexOf("\">",result.indexOf(namex,shift)+namex.length());
                int nameend = result.indexOf("</a>",result.indexOf(namex,shift)+1+namex.length());
                name[i] = result.substring(namehead+2,nameend);
                if(nameend-namehead>20){name[i] = result.substring(namehead+2,namehead+20)+"..";}

                int titlehead = result.indexOf("\">",result.indexOf(titlex,shift)+titlex.length());
                int titleend = result.indexOf("</a>",result.indexOf(titlex,shift)+1+titlex.length());
                title[i] = result.substring(titlehead+2,titleend);
                if(titleend-titlehead>50){title[i] = result.substring(titlehead+2,titlehead+50)+"..";}

            }
            
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e); //FIXME
        } catch (IOException e) {
            throw new RuntimeException(e); //FIXME
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

            MainActivity m = new MainActivity();
           // final GridView gv = (GridView)View.findViewById(R.id.gridView);
           // gv.setAdapter(new SampleGridViewAdapter(this));
           // gv.setOnScrollListener(new SampleScrollListener(this));
            /*
            for(int i=0;i<60;i++) {
                Picasso.with(mContext).load("http://lohas.nicoseiga.jp//thumb/" + imageId[i] + "i?").into();
            }
            */
    	}
		return null;
    }

    @Override
    protected void onPostExecute(String result) {
		final ImageView img = (ImageView)rootView.findViewById(R.id.imageView1);
		Picasso.with(mContext).load("http://lohas.nicoseiga.jp//thumb/"+imageId[page]+"i?").into(img);
        int pos;
        if(isf){pos = page-1;}else{pos=page+1;}
        MainActivity m = new MainActivity();
        m.uri = Uri.parse("http://seiga.nicovideo.jp/seiga/im"+imageId[pos]);
        snackbar = new SnackBar(this.mainActivity, "["+name[pos]+"] "+title[pos], null, null);
        snackbar.show();

    }

}