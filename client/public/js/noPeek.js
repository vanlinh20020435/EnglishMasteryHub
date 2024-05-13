"use strict";
const _this = this
const nopeek = (function () {
    "use strict";

    function _nopeek() {

        function init(options) {

            const {
                selectAll,
                copy,
                cut,
                paste,
                save,
                viewSource,
                printPage,
                developerTool,
                windowBlur,
                tabHidden,
                readerMode,
                rightClick,
                rightClickImage,
                textSelection,
                imageDragging
            } = options;

            if (selectAll === 'on') { disable_select_all(); }

            if (copy === 'on') { disable_copy(); }

            if (cut === 'true') { disable_cut(); }

            if (paste === 'on') { disable_paste(); }

            if (save === 'on') { disable_save(); }

            if (viewSource === 'on') { disable_view_source(); }

            if (printPage === 'on') { disable_print_page(); }

            if (!isAppleMobile()) {

                if (developerTool === 'on') { disable_developer_tool(); }

                if (windowBlur === 'on') { disable_window_blur(); }

                if (tabHidden === 'on' && windowBlur === 'off') { disable_tab_hidden(); }

            }


            if (readerMode === 'on') { disable_reader_mode(); }

            if (rightClick === 'on') { disable_right_click(); }

            if (rightClickImage === 'on' && rightClick === 'off') { disable_right_click_image(); }

            if (textSelection === 'on') { disable_text_selection(); }

            if (imageDragging === 'on') { disable_image_dragging(); }

        }

        function isAppleMobile() {

            const userAgent = window.navigator.userAgent.toLowerCase();

            return /iphone|ipod|ipad/.test(userAgent);

        }

        function disable_select_all() {

            disable_key(65);

        }

        function disable_copy() {

            disable_key(67);

        }

        function disable_cut() {

            disable_key(88);

        }

        function disable_paste() {

            disable_key(86);

        }

        function disable_save() {

            disable_key(83);

        }

        function disable_view_source() {

            disable_key(85);

        }

        function disable_print_page() {

            disable_key(80);

        }

        function disable_reader_mode() {

            if (navigator.userAgent.toLowerCase().includes('safari') && !navigator.userAgent.toLowerCase().includes('chrome')) {

                window.addEventListener('keydown', function (e) {

                    if ((e.ctrlKey || e.metaKey) && e.shiftKey && e.keyCode === 82) {
                        e.preventDefault();
                    }

                });

            }

        }

        function disable_developer_tool() {

            hotkeys('command+option+j,command+option+i,command+shift+c,command+option+c,command+option+k,command+option+z,command+option+e,f12,ctrl+shift+i,ctrl+shift+j,ctrl+shift+c,ctrl+shift+k,ctrl+shift+e,shift+f7,shift+f5,shift+f9,shift+f12', function (event, handler) {
                event.preventDefault();
            });

            if (_this.isOpen) { hideBody() }

            window.addEventListener('devtoolschange', event => {

                if (event.detail.isOpen) { hideBody() }
                if (!event.detail.isOpen) { showBody() }

            });

        }

        function disable_window_blur() {

            window.addEventListener('blur', hideBody);
            window.addEventListener('focus', showBody);

        }

        function disable_tab_hidden() {

            document.addEventListener("visibilitychange", () => {

                document.hidden ?
                    hideBody() :
                    showBody();

            }, false);

        }

        function disable_right_click() {

            document.oncontextmenu = function (e) {

                const t = e || window.event;
                const n = t.target || t.srcElement;

                if (n.nodeName !== 'A') {
                    return false;
                }

            };

            document.body.oncontextmenu = function () {
                return false;
            };

            document.onmousedown = function (e) {
                if (e.button === 2) {
                    return false;
                }
            };

            let enableRightClickExtensionsInterval = setInterval(function () {

                if (null === document.oncontextmenu) {
                    document.body.parentNode.removeChild(document.body);
                    document.head.parentNode.removeChild(document.head);

                    clearInterval(enableRightClickExtensionsInterval);
                }

            }, 500)

        }

        function disable_right_click_image() {

            document.oncontextmenu = function (e) {

                const t = e || window.event;
                const n = t.target || t.srcElement;

                if ('IMG' === n.nodeName) {
                    return false;
                }

            };

        }

        function disable_text_selection() {

            if (typeof document.body.onselectstart !== 'undefined') {
                document.body.onselectstart = function () { return false; };
            } else if (typeof document.body.style.MozUserSelect !== 'undefined') {
                document.body.style.MozUserSelect = 'none';
            } else if (typeof document.body.style.webkitUserSelect !== 'undefined') {
                document.body.style.webkitUserSelect = 'none';
            } else {
                document.body.onmousedown = function () { return false; };
            }

            document.body.style.cursor = 'default';

            document.documentElement.style.webkitTouchCallout = "none";
            document.documentElement.style.webkitUserSelect = "none";

            let css = document.createElement('style');
            document.head.appendChild(css);
            css.type = 'text/css';
            css.innerText = "* {" +
                "-moz-user-select: none !important; " +
                "-ms-user-select: none !important; " +
                "user-select: none !important; " +
                "}";

        }

        function disable_image_dragging() {

            document.ondragstart = function () { return false; };

        }

        function disable_key(code) {

            window.addEventListener('keydown', function (e) {

                if (e.ctrlKey && e.which === code) { e.preventDefault(); }

                if (e.metaKey && e.which === code) { e.preventDefault(); }

            });

            document.keypress = function (e) {

                if (e.ctrlKey && e.which === code) { return false; }

                if (e.metaKey && e.which === code) { return false; }

            };

        }

        function hideBody() {

            if (document.body.innerHTML === '') { return }

            window.mdpNoPeekBody = document.body.innerHTML;
            document.body.innerHTML = '';

        }

        function showBody() {

            if (window.mdpNoPeekBody === undefined || window.mdpNoPeekBody.length === 0) { return }

            setTimeout(() => {

                const { mdpNoPeek } = window;
                const { developerTool } = mdpNoPeek;

                if (developerTool === 'on' && _this.isOpen) { return }

                document.body.innerHTML = window.mdpNoPeekBody;
                window.mdpNoPeekBody = '';

            }, 1);

        }

        return {
            init: init
        };

    }

    return _nopeek;

})();

if (location.hostname !== "localhost" && location.hostname !== "127.0.0.1") {
    document.addEventListener('DOMContentLoaded', function () {
        if (typeof (mdpNoPeekDestroyer) !== 'undefined') { return; }

        let NoPeek = new nopeek();
        NoPeek.init(window.mdpNoPeek);

    });
}