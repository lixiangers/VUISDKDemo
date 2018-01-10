package com.roobo.ratn.demo.grammar;

import com.roobo.vui.common.vocon.BnfGrammar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixiang on 18-1-8.
 * 测试动态加载，enable/disable 离线文件
 */

public class GrammarManager {
    private static GrammarManager instance = null;
    private List<BnfGrammar> bnfGrammars;
    private List<GrammarViewModel> viewModels;

    private boolean isLoaded;

    private GrammarManager() {
        bnfGrammars = new ArrayList<>();
        viewModels = new ArrayList<>();
        bnfGrammars.add(new BnfGrammar("test_dynamic_offline_1"));
        bnfGrammars.add(new BnfGrammar("test_dynamic_offline_2"));
    }

    public synchronized static GrammarManager getInstance() {
        if (instance == null) {
            instance = new GrammarManager();
        }
        return instance;
    }

    public List<BnfGrammar> getBnfGrammars() {
        return bnfGrammars;
    }

    public void addViewModels() {
        isLoaded = true;
        for (BnfGrammar bnfGrammar : bnfGrammars) {
            viewModels.add(new GrammarViewModel(bnfGrammar));
        }
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public List<GrammarViewModel> getViewModels() {
        return viewModels;
    }

    public static class GrammarViewModel {
        private BnfGrammar bnfGrammar;
        private boolean isEnable;

        public GrammarViewModel(BnfGrammar bnfGrammar) {
            this.bnfGrammar = bnfGrammar;
            this.isEnable = true;//加载后默认是可用
        }

        public boolean isEnable() {
            return isEnable;
        }

        public void setEnable(boolean enable) {
            isEnable = enable;
        }

        public BnfGrammar getBnfGrammar() {
            return bnfGrammar;
        }
    }
}
