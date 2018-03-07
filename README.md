# DateSelect
时间选择器

## 使用方法

#### module 下添加

     compile 'com.github.BenYanYi:DateSelect:1.0.1'

 #### project 下添加

    allprojects {
        repositories {
            jcenter()
            maven {
                url 'https://jitpack.io'
            }
        }
    }

##### 带时间的选择器
    SelectData selectData = new SelectData(this);
    selectData.showAtLocation(but, Gravity.BOTTOM, 0, 0);
    selectData.setDateClickListener(new SelectData.OnDateClickListener() {
        @Override
        public void onClick(String year, String month, String day, String hour, String minute) {
            Toast.makeText(MainActivity.this, year + "-" + month + "-" + day + " " + hour + ":" + minute, Toast.LENGTH_SHORT).show();
        }
    });
##### 不带时间的选择器
    SelectData selectData = new SelectData(this,false);
    selectData.showAtLocation(but, Gravity.BOTTOM, 0, 0);
    selectData.setDateClickListener(new SelectData.OnDateClickListener() {
        @Override
        public void onClick(String year, String month, String day, String hour, String minute) {
            Toast.makeText(MainActivity.this, year + "-" + month + "-" + day , Toast.LENGTH_SHORT).show();
        }
    });
