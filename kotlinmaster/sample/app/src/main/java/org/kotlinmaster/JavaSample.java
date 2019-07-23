package org.kotlinmaster;

import android.app.Activity;

public class JavaSample {

    void main() {
        Activity activity = new NewActivity();
        if (activity instanceof NewActivity) {
            ((NewActivity) activity).action();
        }
    }

}
