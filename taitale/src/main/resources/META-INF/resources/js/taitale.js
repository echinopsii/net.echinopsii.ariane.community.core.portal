// ┌──────────────────────────────────────────────────────────────────────────────────────┐ \\
// │ Taitale 0.0.1   - JavaScript Taitale Library                                         │ \\
// │ Use Raphael.js                                                                       │ \\
// │ -------------------------------------------------------------------------------------│ \\
// │ Taitale - provide an infrastructure mapping graph engine                             │ \\
// │ Copyright (C) 2013  Mathilde Ffrench												  │ \\
// │ 																					  │ \\
// │ This program is free software: you can redistribute it and/or modify                 │ \\
// │ it under the terms of the GNU Affero General Public License as                       │ \\
// │ published by the Free Software Foundation, either version 3 of the                   │ \\
// │ License, or (at your option) any later version.									  │ \\
// │																					  │ \\
// │ This program is distributed in the hope that it will be useful,					  │ \\
// │ but WITHOUT ANY WARRANTY; without even the implied warranty of						  │ \\
// │ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the						  │ \\
// │ GNU Affero General Public License for more details.								  │ \\
// │																					  │ \\
// │ You should have received a copy of the GNU Affero General Public License			  │ \\
// │ along with this program.  If not, see <http://www.gnu.org/licenses/>.				  │ \\
// └──────────────────────────────────────────────────────────────────────────────────────┘ \\

require.config({
    baseUrl: window.location.protocol + "//" + window.location.host + "/" + window.location.pathname.split('/')[1] + '/javax.faces.resource/',
    paths: {
        'jquery': 'jquery/jquery.js.jsf?ln=primefaces',
        'jquery-private': 'jquery-private.js.jsf?ln=js',
        'jquery-ui': 'jquery/jquery-plugins.js.jsf?ln=primefaces',
        'prime-ui': 'primeui/primeui-0.9.6.js.jsf?ln=js',
        'eve': 'raphael/eve.js.jsf?ln=js',
        'raphael-core': 'raphael/raphael.core.js.jsf?ln=js',
        'raphael-svg': 'raphael/raphael.svg.js.jsf?ln=js',
        'raphael-vml': 'raphael/raphael.vml.js.jsf?ln=js',
        'raphael': 'raphael/raphael.amd.js.jsf?ln=js',
        'raphael-zpd': 'raphael/raphael.zpd.js.jsf?ln=js',
        'raphael.free_transform': 'raphael/raphael.free_transform.js.jsf?ln=js',

        /*taitale tools*/
        'taitale-cylinder': 'taitale/tools/cylinder.js.jsf?ln=js',
        'taitale-dictionaries': 'taitale/tools/dictionaries.js.jsf?ln=js',
        'taitale-ext-string': 'taitale/tools/ext.string.js.jsf?ln=js',
        'taitale-ext-raphael': 'taitale/tools/ext.raphael.js.jsf?ln=js',
        'taitale-helper': 'taitale/tools/helper.js.jsf?ln=js',
        'taitale-loader': 'taitale/tools/loader.js.jsf?ln=js',
        'taitale-params': 'taitale/tools/params.js.jsf?ln=js',
        'taitale-prototypes': 'taitale/tools/prototypes.js.jsf?ln=js',

        /*taitale core*/
        'taitale-map-options': 'taitale/core/map/options.js.jsf?ln=js',
        'taitale-map-matrix': 'taitale/core/map/matrix.js.jsf?ln=js',
        'taitale-map': 'taitale/core/map/map.js.jsf?ln=js',
        'taitale-container-matrix': 'taitale/core/container/matrix.js.jsf?ln=js',
        'taitale-container-hat': 'taitale/core/container/hat.js.jsf?ln=js',
        'taitale-container': 'taitale/core/container/container.js.jsf?ln=js',
        'taitale-node': 'taitale/core/node/node.js.jsf?ln=js',
        'taitale-endpoint': 'taitale/core/endpoint.js.jsf?ln=js',
        'taitale-transport': 'taitale/core/transport/transport.js.jsf?ln=js',
        'taitale-transport-multicastbus': 'taitale/core/transport/multicastBus.js.jsf?ln=js',
        'taitale-link': 'taitale/core/link.js.jsf?ln=js',

        /*taitale tree layout*/
        'taitale-tree': 'taitale/layout/tree/tree.js.jsf?ln=js',
        'taitale-vertex': 'taitale/layout/tree/vertex.js.jsf?ln=js',

        /*taitale network layout*/
        'taitale-map-splitter': 'taitale/layout/network/mapSplitter.js.jsf?ln=js',
        'taitale-layoutntw-registries' : 'taitale/layout/network/registries.js.jsf?ln=js',
        'taitale-datacenter': 'taitale/layout/network/datacenter/datacenter.js.jsf?ln=js',
        'taitale-datacenter-splitter': 'taitale/layout/network/datacenter/dcSplitter.js.jsf?ln=js',
        'taitale-datacenter-matrix': 'taitale/layout/network/datacenter/matrix.js.jsf?ln=js',
        'taitale-area': 'taitale/layout/network/area/area.js.jsf?ln=js',
        'taitale-area-matrix': 'taitale/layout/network/area/matrix.js.jsf?ln=js',
        'taitale-lan': 'taitale/layout/network/lan/lan.js.jsf?ln=js',
        'taitale-lan-matrix': 'taitale/layout/network/lan/matrix.js.jsf?ln=js'
    },
    map: {
        '*': { 'jquery': 'jquery-private' },
        'jquery-private': { 'jquery': 'jquery' }
    },
    shim: {
        "jquery-ui": {
            exports: "$",
            deps: ['jquery']
        },
        "prime-ui": {
            exports: "$",
            deps: ['jquery','jquery-ui']
        }
    }
});

requirejs (
    [
        'prime-ui',
        'taitale-helper',
        'taitale-loader',
        'taitale-dictionaries',
        'taitale-map-options'
    ],
    function ($, helper, loader, dictionaries, mapOptions) {

        var loader_   = new loader(),
            helper_   = new helper(),
            dic       = new dictionaries(),
            options   = new mapOptions(),
            homeURI   = window.location.protocol + "//" + window.location.host + "/" + window.location.pathname.split('/')[1];

        var readyStateCheckInterval = setInterval(function() {
            if (document.readyState === "complete") {
                /*
                 * wait document state is complete and
                 * load here any PrimeFaces JQuery (ie : global $)
                 * object events related to map
                 */
                $(execQuery.jqId).click([loader_, dic], function(){
                    var request = $(mdslQuery.jqId)[0].value
                    var requestURI = homeURI + "/rest/service/map/query?mdsl="+encodeURI(request)
                    helper_.debug(requestURI.toString());
                    options.setURI(requestURI);
                    try {
                        loader_.reloadMap(options);
                        if (options.getLayout()===dic.mapLayout.NTWWW) {
                            document.getElementById('treeOptions').style.display = "none";
                            document.getElementById('networkOptions').style.display = "";
                            for (var i = 0, ii = networkLayoutDisplayOptions.inputs.length; i < ii; i++) {
                                var input = networkLayoutDisplayOptions.inputs[i];
                                if (input.value==="displayDC") {
                                    loader_.displayDC(input.checked);
                                } else if (input.value==="displayArea") {
                                    loader_.displayArea(input.checked);
                                } else if (input.value==="displayLan") {
                                    loader_.displayLan(input.checked);
                                }
                            }
                        } else if (options.getLayout()===dic.mapLayout.TREE) {
                            document.getElementById('treeOptions').style.display = "";
                            document.getElementById('networkOptions').style.display = "none";
                        }
                        helper_.growlMsgs(
                            {
                                severity: 'info',
                                summary: 'Map successfully loaded ',
                                detail: 'Layout: '+options.getLayout()+"<br>Mode: "+options.getMode()
                            }
                        );
                    } catch (e) {
                        helper_.addMsgToGrowl(e);
                        helper_.growlMsgs(
                            {
                                severity: 'error',
                                summary: 'Failed to load map',
                                detail: 'Layout: '+options.getLayout()+"<br>Mode: "+options.getMode(),
                                sticky: true
                            }
                        );
                        console.log(e.stack);
                    }
                });
                $(layoutSelector.jqId).change([loader_, dic], function() {
                    for (var i = 0, ii = layoutSelector.inputs.length; i < ii; i++) {
                        var input = layoutSelector.inputs[i];
                        if (input.checked) {
                            var num = input.value;
                            options.setLayout(num);
                            //options.setURI(homeURI + "/rest/service/map/all");
                            try {
                                loader_.reloadMap(options);
                                if (options.getLayout()===dic.mapLayout.NTWWW) {
                                    document.getElementById('treeOptions').style.display = "none";
                                    document.getElementById('networkOptions').style.display = "";
                                    for (var i = 0, ii = networkLayoutDisplayOptions.inputs.length; i < ii; i++) {
                                        var input = networkLayoutDisplayOptions.inputs[i];
                                        if (input.value==="displayDC") {
                                            loader_.displayDC(input.checked);
                                        } else if (input.value==="displayArea") {
                                            loader_.displayArea(input.checked);
                                        } else if (input.value==="displayLan") {
                                            loader_.displayLan(input.checked);
                                        }
                                    }
                                } else if (options.getLayout()===dic.mapLayout.TREE) {
                                    document.getElementById('treeOptions').style.display = "";
                                    document.getElementById('networkOptions').style.display = "none";
                                }
                                helper_.growlMsgs(
                                    {
                                        severity: 'info',
                                        summary: 'Map successfully loaded ',
                                        detail: 'Layout: '+options.getLayout()+"<br>Mode: "+options.getMode()
                                    }
                                );
                            } catch (e) {
                                helper_.addMsgToGrowl(e);
                                helper_.growlMsgs(
                                    {
                                        severity: 'error',
                                        summary: 'Failed to load map',
                                        detail: 'Layout: '+options.getLayout()+"<br>Mode: "+options.getMode(),
                                        sticky: true
                                    }
                                );
                                console.log(e.stack);
                            }
                            break;
                        }
                    }
                });
                $(modeSelector.jqId).change([loader_, dic], function() {
                    for (var i = 0, ii = modeSelector.inputs.length; i < ii; i++) {
                        var input = modeSelector.inputs[i];
                        if (input.checked) {
                            options.setMode(input.value);
                            try {
                                loader_.refreshMap();
                                if (options.getLayout()===dic.mapLayout.NTWWW) {
                                    for (var i = 0, ii = networkLayoutDisplayOptions.inputs.length; i < ii; i++) {
                                        var input = networkLayoutDisplayOptions.inputs[i];
                                        if (input.value==="displayDC") {
                                            loader_.displayDC(input.checked);
                                        } else if (input.value==="displayArea") {
                                            loader_.displayArea(input.checked);
                                        } else if (input.value==="displayLan") {
                                            loader_.displayLan(input.checked);
                                        }
                                    }
                                }
                                helper_.growlMsgs(
                                    {
                                        severity: 'info',
                                        summary: 'Map successfully refreshed ',
                                        detail: 'Layout: '+options.getLayout()+"<br>Mode: "+options.getMode()
                                    }
                                );
                            } catch (e) {
                                helper_.addMsgToGrowl(e);
                                helper_.growlMsgs(
                                    {
                                        severity: 'error',
                                        summary: 'Failed to refresh map',
                                        detail: 'Layout: '+options.getLayout()+"<br>Mode: "+options.getMode(),
                                        sticky: true
                                    });
                                console.log(e.stack);
                            }
                        }
                    }
                });
                $(notificationsOptions.jqId).change([loader_, dic], function() {
                    for (var i = 0, ii = notificationsOptions.inputs.length; i < ii; i++) {
                        var input = notificationsOptions.inputs[i];
                        if (input.value==="notifyInfos") {
                            helper_.setNotifyInfo(input.checked);
                        } else if (input.value==="notifyWarns") {
                            helper_.setNotifyWarn(input.checked);
                        } else if (input.value==="notifyErrs") {
                            helper_.setNotifyErrs(input.checked);
                        }
                    }
                });
                $(networkLayoutDisplayOptions.jqId).change([loader_, dic], function() {
                    for (var i = 0, ii = networkLayoutDisplayOptions.inputs.length; i < ii; i++) {
                        var input = networkLayoutDisplayOptions.inputs[i];
                        if (input.value==="displayDC") {
                            loader_.displayDC(input.checked);
                        } else if (input.value==="displayArea") {
                            loader_.displayArea(input.checked);
                        } else if (input.value==="displayLan") {
                            loader_.displayLan(input.checked);
                        }
                    }
                });
                $(rootTreeSorting.jqId).change([loader_, dic], function() {
                    for (var i = 0, ii = rootTreeSorting.inputs.length; i < ii; i++) {
                        var input = rootTreeSorting.inputs[i];
                        if (input.checked) {
                            var value = input.value;
                            try {
                                options.setRootTreeSorting(value);
                                loader_.sortRootTree(value);
                                loader_.rebuildMapTreeLayout();
                                loader_.refreshMap();
                                helper_.growlMsgs(
                                    {
                                        severity: 'info',
                                        summary: 'Map successfully refreshed ',
                                        detail: 'Name: '+$('#test').val()+'<br>Layout: '+options.getLayout()+"<br>Mode: "+options.getMode()
                                    }
                                );
                            } catch (e) {
                                helper_.addMsgToGrowl(e);
                                helper_.growlMsgs(
                                    {
                                        severity: 'error',
                                        summary: 'Failed refresh tree map with selected root tree sorting',
                                        detail: 'Check the console log to know more...',
                                        sticky: true
                                    }
                                );
                                console.log(e.stack);
                            }
                            break;
                        }
                    }
                });
                $(subTreesSorting.jqId).change([loader_, dic], function() {
                    for (var i = 0, ii = subTreesSorting.inputs.length; i < ii; i++) {
                        var input = subTreesSorting.inputs[i];
                        if (input.checked) {
                            var value = input.value;
                            try {
                                options.setSubTreesSorting(value);
                                loader_.sortSubTrees(options.getSubTreesSorting());
                                loader_.rebuildMapTreeLayout();
                                loader_.refreshMap();
                                helper_.growlMsgs(
                                    {
                                        severity: 'info',
                                        summary: 'Map successfully refreshed ',
                                        detail: 'Name: '+$('#test').val()+'<br>Layout: '+options.getLayout()+"<br>Mode: "+options.getMode()
                                    }
                                );
                            } catch (e) {
                                helper_.addMsgToGrowl(e);
                                helper_.growlMsgs(
                                    {
                                        severity: 'error',
                                        summary: 'Failed refresh tree map with selected sub trees sorting',
                                        detail: 'Check the console log to know more...',
                                        sticky: true
                                    }
                                );
                                console.log(e.stack);
                            }
                            break;
                        }
                    }
                });
                for (var i = 0, ii = notificationsOptions.inputs.length; i < ii; i++) {
                    var input = notificationsOptions.inputs[i];
                    if (input.value==="notifyInfos") {
                        input.checked=helper_.getNotifyInfo();
                    } else if (input.value==="notifyWarns") {
                        input.checked=helper_.getNotifyWarn();
                    } else if (input.value==="notifyErrs") {
                        input.checked=helper_.getNotifyErrs();
                    }
                }
                clearInterval(readyStateCheckInterval);
            }
        }, 10);

        helper_.initGrowlMsgs(widget_growl.jqId);

        for (var i = 0, ii = layoutSelector.inputs.length; i < ii; i++) {
            var input = layoutSelector.inputs[i];
            if (input.checked) {
                var num = input.value;
                options.setLayout(num);
                //options.setURI(homeURI + "/rest/service/map/all");
                break;
            }
        }

        for (var i = 0, ii = modeSelector.inputs.length; i < ii; i++) {
            var input = modeSelector.inputs[i];
            if (input.checked) {
                options.setMode(input.value);
            }
        }

        /*
        try {
            if (options.getLayout()!==dic.mapLayout.NTWWW) {
                document.getElementById('networkOptions').style.display = "none";
            } else {
                document.getElementById('networkOptions').style.display = "";
            }
            loader_.loadMap(options);
            for (var i = 0, ii = networkLayoutDisplayOptions.inputs.length; i < ii; i++) {
                var input = networkLayoutDisplayOptions.inputs[i];
                if (input.value==="displayDC") {
                    loader_.displayDC(input.checked);
                } else if (input.value==="displayArea") {
                    loader_.displayArea(input.checked);
                } else if (input.value==="displayLan") {
                    loader_.displayLan(input.checked);
                }
            }
            helper_.growlMsgs(
                {
                    severity: 'info',
                    summary: 'Map successfully loaded',
                    detail: 'Layout: '+options.getLayout()+"<br>Mode: "+options.getMode()
                }
            );
        } catch (e) {
            helper_.addMsgToGrowl(e);
            helper_.growlMsgs(
                {
                    severity: 'error',
                    summary: 'Failed to load map',
                    detail: 'Layout: '+options.getLayout()+"<br>Mode: "+options.getMode(),
                    sticky: true
                });
            console.log(e.stack);
        }
        */
    });