/*
 * Copyright (c) 2015.  The AppCan Open Source Project.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package org.zywx.wbpalmstar.engine.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.view.View;
import android.webkit.WebView;

import java.lang.reflect.Method;

import org.zywx.wbpalmstar.acedes.EXWebViewClient;
import org.zywx.wbpalmstar.engine.EBrowserBaseSetting;

/**
 * Created by ylt on 15/8/24.
 */
public class ACEWebView extends WebView {

    // use for debug
    protected Method mDumpDisplayTree;
    protected Method mDumpDomTree;
    protected Method mDumpRenderTree;
    protected Method mDrawPage;

    protected Method mDismissZoomControl;
    
	private EBrowserBaseSetting mBaSetting;
	private EXWebViewClient mEXWebViewClient;

    public ACEWebView(Context context) {
        super(context);
    }

    protected void init(boolean webApp){
		int version = Build.VERSION.SDK_INT;
		if (version >= Build.VERSION_CODES.HONEYCOMB && !isHardwareAccelerated()) {
			setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
    	if (version <= 7) {
			if (mBaSetting == null) {
				mBaSetting = new EBrowserSetting(this);
				mBaSetting.initBaseSetting(webApp);
				setWebViewClient(mEXWebViewClient = new CBrowserWindow());
				setWebChromeClient(new CBrowserMainFrame(getContext()));
			}
			
		} else {
			
			if (mBaSetting == null) {
				mBaSetting = new EBrowserSetting7(this);
				mBaSetting.initBaseSetting(webApp);
				setWebViewClient(mEXWebViewClient = new CBrowserWindow7());
				setWebChromeClient(new CBrowserMainFrame7(getContext()));
			}
			
		}
    }
    
    @SuppressLint("NewApi")
	public void pauseCore() {
        if (Build.VERSION.SDK_INT >= 11) {
            super.onPause();
        } else {
            try {
                Class[] nullParm = {};
                Method pause = WebView.class.getDeclaredMethod("onPause",
                        nullParm);
                pause.setAccessible(true);
                pause.invoke(this);
            } catch (Exception e) {
                ;
            }
        }
    }


    public void resumeCore() {
        if (Build.VERSION.SDK_INT >= 11) {
            super.onResume();
        } else {
            try {
                Class[] nullParm = {};
                Method resume = WebView.class.getDeclaredMethod("onResume",
                        nullParm);
                resume.setAccessible(true);
                resume.invoke(this);
            } catch (Exception e) {
                ;
            }
        }
    }

    public void initPrivateVoid() {
        Class[] nullParm = {};
        try {
            mDismissZoomControl = WebView.class.getDeclaredMethod(
                    "dismissZoomControl", nullParm);
            mDismissZoomControl.setAccessible(true);
        } catch (Exception e) {
            ;
        }

        try {
            mDumpDisplayTree = WebView.class.getDeclaredMethod(
                    "dumpDisplayTree", nullParm);
            mDumpDisplayTree.setAccessible(true);
        } catch (Exception e) {
            ;
        }
        Class[] booleanParam = { boolean.class };
        try {
            mDumpDomTree = WebView.class.getDeclaredMethod("dumpDomTree",
                    booleanParam);
            mDumpDomTree.setAccessible(true);
        } catch (Exception e) {
            ;
        }
        try {
            mDumpRenderTree = WebView.class.getDeclaredMethod("dumpRenderTree",
                    booleanParam);
            mDumpRenderTree.setAccessible(true);
        } catch (Exception e) {
            ;
        }
        try {
            Class[] canvasParam = { Canvas.class };
            mDrawPage = WebView.class
                    .getDeclaredMethod("drawPage", canvasParam);
            mDrawPage.setAccessible(true);
        } catch (Exception e) {
            ;
        }
        if (Build.VERSION.SDK_INT >= 9) {
            setOverScrollMode(2);
            return;
        }
        try {
            Class[] intParam = { int.class };
            Method setOverScrollMode = WebView.class.getDeclaredMethod(
                    "setOverScrollMode", intParam);
            setOverScrollMode.invoke(this, 2);
        } catch (Exception e) {
            ;
        }
    }
    
    @Override
    public void destroy(){
		mBaSetting = null;
    }

	public void setDefaultFontSize(int size) {
		mBaSetting.setDefaultFontSize(size);
	}

	public void setSupportZoom() {
		mBaSetting.setSupportZoom();
	}
    
	public void setDownloadListener(){
		
	}
}
