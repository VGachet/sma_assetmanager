# SMAAssetManager

This library is linked to the following dependencies :

    :::javascript    
    compile 'fr.smartapps.smartguide:downloader_library:0.3.1'
    compile 'fr.smartapps.smartguide:googleplayservices_lib:0.3.1'
    compile 'fr.smartapps.smartguide:play_licensing:0.3.1'
    compile 'fr.smartapps.smartguide:zip_file:0.3.1'
    compile 'commons-io:commons-io:2.4'
    compile 'com.github.bumptech.glide:glide:3.7.0'

This library has been created to help you manage easily every image, audio, video, ... from any places :    

- to get assets from assets folder : "assets://filename.png"  
- to get assets from expansion file : "obb://filename.png"  
- to get assets from external public phone directory : "external://filename.png"  
- to get assets from external private phone directory : "external_private://filename.png"  

To use OBB you must have those permissions in your manifest : 

    :::xml
    <!-- OBB PERMISSION -->
    <uses-permission android:name="com.android.vending.CHECK_LICENSE"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.WAKE_LOCK"/>
    <uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES"/>

If you don't use OBB, you need this permission to write and read in external storage :

    :::xml
    <!-- EXTERNAL STORAGE PERMISSION -->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

# Import

    :::javascript
    // project gradle 
    allprojects {
        repositories {
            jcenter()
            maven { url 'https://dl.bintray.com/smartapps/maven' }
        }
    }

    // module gradle
    compile 'fr.smartapps.library:lib_smaassetmanager:1.0.16'

# Implement

## Initialize

Initialize **SMAAssetManager** first

    assetManager = new SMAAssetManager(this);

If you wanna use Expansion file (APK over 100MB), you have to configure it with :

    assetManager.initMainOBB(version, size);

If no suffix has been set, you can set a default folder where you will take every file (if none of these line are set, assets folder is the default one) :

    :::java
    assetManager.setDefaultStorageType(SMAAssetManager.STORAGE_TYPE_ASSETS);
    // or
    assetManager.setDefaultStorageType(SMAAssetManager.STORAGE_TYPE_OBB);
    // or
    assetManager.setDefaultStorageType(SMAAssetManager.STORAGE_TYPE_EXTERNAL);
    // or
    assetManager.setDefaultStorageType(SMAAssetManager.STORAGE_TYPE_EXTERNAL_PRIVATE);

    // by default when you are searching file depending on DEFAULT_STORAGE : it will add this extension right before your file name
    assetManager.setExtensionDirectory("package_name/");

## Drawable

![](/screen_drawable.png) 

    :::java
    imageView1.setBackground(assetManager.getDrawable("obb://pikachu.png"));
    imageView2.setBackground(assetManager.getDrawable("external_private://pikachu.png"));
    imageView3.setBackground(assetManager.getDrawable("external://pikachu.png"));

## Custom drawable

![Alt text](/screen_image.png) 

    :::java
    imageView1.setBackground(assetManager.getDrawable("assets://tinder.png"));
    imageView2.setBackground(assetManager.getDrawable("assets://tinder.png").filter("#000000"));
    imageView3.setBackground(assetManager.getDrawable("assets://tinder.png").alpha(100));

## Button

![Alt text](/screen_button.png) 

    :::java
    buttonView.setBackground(assetManager.getStateListDrawable()
        .focused("assets://tinder_selected.png")
        .pressed("assets://tinder_selected.png")
        .inverse("assets://tinder.png"));    

    // always finish with inverse state : it is the default state
        
    toggleButtonView.setBackground(assetManager.getStateListDrawable()
        .checked("assets://tinder_selected.png")
        .inverse("assets://tinder.png"));

    // known bug : you cannot put modified drawable (drawable.filter(), drawable.alpha(), ...) in StateListDrawable before Lollipop

## TextView

![Alt text](/screen_textview.png) 

    :::java
    textView1.setTypeface(assetManager.getTypeFace("assets://font_assets.ttf"));
    textView2.setTypeface(assetManager.getTypeFace("external://font_external.ttf"));
    textView3.setTypeface(assetManager.getTypeFace("external_private://font_external_private.ttf"));
    textView4.setTypeface(assetManager.getTypeFace("obb://font_obb.ttf"));
    textView5.setTextColor(assetManager.getStateListColor().focused("#abcdef").pressed("#abcdef").inverse("#abcabc"));

## Audio

![Alt text](/screen_audio.png) 

    :::java
    SMAAudioPlayer audioPlayer = assetManager.getAudioPlayer(url, new SMAAudioPlayerListener() {
        @Override
        public void onSongProgress(int progress, int totalProgress) {
            // callback as long as the song is playing
        }

        @Override
        public void onSongFinish(int totalProgress) {
            // callback when the song is finished
        }
    });

Also customize your SeekBar :

    :::java
    seekBar.setProgressDrawable(assetManager.getLayerDrawable()
        .backgroundProgressColor("#cccccc")
        .progressColor("#abcdef")
        .getLayer());
    SMADrawable thumb = assetManager.getDrawable("assets://scrubber_control.png").filter("#abcdef").density(5);
    seekBar.setThumb(thumb);

## Webview

Load web content from url path :

    :::java
    SMAWebView webView = (SMAWebView) findViewById(R.id.webview);
    assetManager.setDefaultStorageType(SMAAssetManager.STORAGE_TYPE_ASSETS);
    webView.loadPath("html/index.html", assetManager, new SMAWebViewListener() {
        @Override
        public void onUrlLoadProgress(int progress, int totalProgress) {
            // only use when loading from http url. SMAWebview starts invisible 
            // and became progressively visible when web content are loaded
        }

        @Override
        public void onUrlCall(String url) {
            // callback called everytime your content redirect to another url
        }
    });

Load content from template & base directory : 

    :::java
    // baseDirectory is a folder inside "default storage" and then inside "extension directory"
    // templateHTML is a full html string
    webview.loadTemplate(baseDirectory, templateHTML, assetManager, webViewListener);
    // or
    webview.loadTemplate(templateHTML, assetManager, webViewListener);

## NEW ! Asynchronous loading with glide (recommended by Google)

![](/glide.gif)

    :::java
    ImageView imageAssets = (ImageView) findViewById(R.id.image_1);
    Glide.with(this).load(new SMAFile("assets://pikachu.png", assetManager)).into(imageAssets);

    ImageView imageExternal = (ImageView) findViewById(R.id.image_2);
    Glide.with(this).load(new SMAFile("external_private://pikachu.png", assetManager)).into(imageExternal);

    ImageView imageExternalPrivate = (ImageView) findViewById(R.id.image_3);
    Glide.with(this).load(new SMAFile("external://pikachu.png", assetManager)).into(imageExternalPrivate);

    ImageView imageOBB = (ImageView) findViewById(R.id.image_4);
    Glide.with(this).load(new SMAFile("obb://pikachu.png", assetManager)).into(imageOBB);

## Find much more power in the sample !

## TODO

* Gérer téléchargement OBB depuis le store si non présent
* Gérer OutOfMemoryError, drawable vs bitmap