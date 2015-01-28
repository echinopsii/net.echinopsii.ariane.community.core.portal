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
    baseUrl: window.location.protocol + "//" + window.location.host + "/" + window.location.pathname.split('/')[1],
    paths: {
        'jquery': 'ajs/jquery/jquery-1.9.1',
        'jquery-ui': 'ajs/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom',
        'prime-ui': 'ajs/primeui-0.9.6/development/primeui-0.9.6'
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
        'prime-ui'
    ],
    function ($) {

        $('#dlg').puidialog({
            showEffect: 'fade',
            hideEffect: 'fade',
            minimizable: true,
            maximizable: false,
            modal: false,
            buttons: [{
                text: 'Yes',
                icon: 'ui-icon-check',
                click: function() {
                    $('#dlg').puidialog('hide');
                }
            },
                {
                    text: 'No',
                    icon: 'ui-icon-close',
                    click: function() {
                        $('#dlg').puidialog('hide');
                    }
                }
            ]
        });

        $('#btn-show').puibutton({
            icon: 'ui-icon-arrow-4-diag',
            click: function() {
                $('#dlg').puidialog('show');
            }
        });

        $('#inputTxt').puiinputtext();

        $('#fieldSetDefault').puifieldset();

        $('#fieldSetToggle').puifieldset({
            toggleable: true
        });

        $('#defaultAccordion').puiaccordion();

        $('#multipleAccordion').puiaccordion({multiple:true});

        $('#defaultTab').puitabview();

        $('#dynamicTab').puitabview({
            change: function(event, ui) {

                $.ajax({
                    type: "GET",
                    url: './tabcontent',
                    data: {tabindex: ui.index},
                    dataType: "html",
                    context: this,
                    success: function(data) {
                        $(this).children('div.ui-tabs-panels').children().eq(ui.index).html(data);
                    }
                });
            }
        });


        $('#effectTab').puitabview({
            effect: {
                name: 'fade'
                ,duration: 'normal'
            }
        });

        $('#closableTab').puitabview();

        $('#leftTab').puitabview({
            orientation: 'left'
        });

        $( "#datepicker" ).datepicker();
        
        //Login Box
        $('#loginboxUser').puiinputtext(); 
        $('#loginboxPassword').puipassword();  
		$('#loginboxSubmit').puibutton();  
    });