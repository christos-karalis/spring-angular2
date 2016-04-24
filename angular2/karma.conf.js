module.exports = function(config) {
    config.set({

//        basePath: '.',
//
        frameworks: ['jasmine'],

        files: [
            // paths loaded by Karma
            {pattern: 'node_modules/angular2/bundles/angular2-polyfills.js', included: true, watched: true},
            {pattern: 'node_modules/systemjs/dist/system.src.js', included: true, watched: true},
            {pattern: 'node_modules/rxjs/bundles/Rx.js', included: true, watched: true},
            {pattern: 'node_modules/angular2/bundles/angular2.dev.js', included: true, watched: true},
            {pattern: 'node_modules/angular2/bundles/testing.dev.js', included: true, watched: true},
            {pattern: 'node_modules/angular2/bundles/http.dev.js', included: true, watched: true},
            {pattern: 'node_modules/angular2/bundles/router.dev.js', included: true, watched: true},
            {pattern: 'karma-test-shim.js', included: true, watched: true},

            // paths loaded via module imports
            {pattern: 'build/webapp/app/**/*.js', included: false, watched: true},
            {pattern: 'src/main/webapp/app/**/*.ts', included: false, watched: true},
            {pattern: 'build/webapp/app/**/*.js.map', included: false, watched: true},
            {pattern: 'src/main/webapp/main/**/*.html', included: false, watched: true}

        ],

        proxies: {
            // required for component assests fetched by Angular's compiler
            '/main/': '/base/src/main/webapp/main/'
        },

        port: 9877,

        logLevel: config.LOG_INFO,

        colors: true,
        autoWatch: true,
        browsers: ['Chrome'],

        // Karma plugins loaded
        plugins: [
            'karma-jasmine',
            'karma-coverage',
            'karma-chrome-launcher'
        ],

        // Coverage reporter generates the coverage
        reporters: ['progress', 'dots', 'coverage'],

        // Source files that you wanna generate coverage for.
        // Do not include tests or libraries (these files will be instrumented by Istanbul)
        preprocessors: {
            'src/**/!(*spec).js': ['coverage']
        },

        coverageReporter: {
            reporters:[
                {type: 'json', subdir: '.', file: 'coverage-final.json'}
            ]
        }//,

      //  singleRun: true
    })
};
