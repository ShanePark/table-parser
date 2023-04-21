<script>
    import Highlight, {LineNumbers} from "svelte-highlight";
    import {bash} from "svelte-highlight/languages"
    import "svelte-highlight/styles/github-dark-dimmed.css"

    export let tableValue;
    let currentUrl;
    if (typeof window !== 'undefined') {
        currentUrl = window.location.href;
    }
    $: code =
        `curl --request POST '${currentUrl}api/excel' \\
  --header 'Content-Type: text/plain' \\
  --data-raw '${tableValue}' \\
  --output ~/Downloads/file.xlsx`;
</script>

<div class="hljs-code">
  <Highlight language={bash} {code} let:highlighted>
    <LineNumbers {highlighted}/>
  </Highlight>
</div>

<style>
    .hljs-code {
        margin-top: 30px;
        max-height: 300px;
        overflow: scroll;
    }
</style>
