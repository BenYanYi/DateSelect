# DateSelect
时间选择器

## 使用方法

#### module 下添加

     compile 'com.github.BenYanYi:DateSelect:1.0.2'

 #### project 下添加

    allprojects {
        repositories {
            jcenter()
            maven {
                url 'https://jitpack.io'
            }
        }
    }
    
### 1.0.2版本
    SelectData selectData = new SelectData(this,selectType);
    
    其余与1.0.1版本一样
    
    SelectType 表示需要隐藏的段落
    
    /**
     * 不隐藏，隐藏时，隐藏分
     */
     NONE, HOUR, MIN  
     
     添加SelectPeriod(时间段选择，结束时间需大于开始时间，且不能大于当前时间)
     用法和时间选择器一样(SelectData)
    
### 1.0.1版本
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
