<script>
    import CurlSample from './curl.svelte';
    import {Button, ButtonGroup, Col, Input, Row, Styles} from 'sveltestrap';
    import {onMount} from "svelte";

    let tableValue;
    const fileNames = ['default', 'lol', 'programming', 'laptop'];

    let loadSample = (event) => {
        document.querySelectorAll('.samples button')
            .forEach(button => button.classList.remove('active'));
        let target = event.target;
        target.classList.add('active');
        fetch(`./samples/${(target.innerText)}.html`)
            .then(response => response.text())
            .then(text => {
                tableValue = text;
            });
    }

    function downloadExcel() {
        const req = new XMLHttpRequest();
        req.open('POST', '/api/excel', true);
        req.responseType = 'arraybuffer';
        req.send(tableValue);

        req.onload = function () {
            if (req.status !== 200) {
                // convert req.response to string
                const body = new TextDecoder("utf-8").decode(req.response);
                alert('[Error] : ' + body);
                return;
            }
            const arrayBuffer = req.response;
            if (!arrayBuffer) return;
            const blob = new Blob([arrayBuffer], {type: "application/octetstream"});
            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            let filename = document.querySelectorAll('.samples button.active')[0].innerText;
            link.download = `${filename}.xlsx`;
            link.click();
        };
    }

    onMount(() => {
        document.querySelector('.samples button').click();
    });

    function copy() {
        let code = '';
        const rows = document.querySelectorAll('.curl-container .hljs-code tr td:last-child');
        rows.forEach((row, index) => {
            code += row.textContent.trim();
            if (index < rows.length - 1) {
                code += ' ';
            }
        });

        // Remove the backslashes and any trailing spaces.
        code = code.replace(/\\[ \t]*\n*/g, '');

        navigator.clipboard.writeText(code)
            .then(() => {
                alert('Copied to clipboard');
            })
            .catch(err => {
                console.error('Could not copy text: ', err);
                alert('Failed to copy text to clipboard');
            });
    }

</script>
<Styles/>
<div class="container">
  <Row style="margin-bottom: 10px;">
    <Col style="text-align:right;">
      <span class="title">Table Parser Demo page</span>
      <Button class="xlsx" on:click={downloadExcel}>Download Excel</Button>
      <ButtonGroup class="samples">
        {#each fileNames as fileName}
          <Button on:click={(e) => loadSample(e)}>{fileName}</Button>
        {/each}
      </ButtonGroup>
    </Col>
  </Row>
  <Row>
    <Col>
      <div class="preview">{@html tableValue}</div>
    </Col>
    <Col>
      <Input type="textarea" cols="50" rows="23" bind:value="{tableValue}" style="height:550px;"/>
    </Col>
  </Row>
  <div class="curl-container">
    <div class="curl-header">
      <span>Curl Sample</span>
      <Button class="button-gap" on:click={copy}>Copy</Button>
    </div>
    <CurlSample bind:tableValue={tableValue}/>
  </div>
</div>

<style>
    .container {
        padding: 20px;
    }

    .button-gap {
        display: inline-block;
        width: 10px;
    }

    .preview {
        height: 550px;
        display: flex;
        overflow: scroll;
    }

    .preview :global(table) {
        width: 100%;
        border-collapse: collapse;
    }

    .preview :global(td), .preview :global(th) {
        border: 1px solid black;
        padding: 5px;
    }

    .preview :global(thead) {
        background-color: #888;
        color: white;
    }

    :global(.sum) {
        background-color: #ddd;
    }

    .curl-container {
        margin-top: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
    }

    .curl-header {
        background-color: #22272e;
        border-bottom: grey 1px solid;
        color: #ccc;
        padding: 5px 5px 5px 15px;
        font-weight: bold;
        border-radius: 4px 4px 0 0;
    }

    :global(.container .hljs-code) {
        margin: 0px;
        padding: 0px;
    }

    .title {
        float: left;
        font-size: 1.2em;
        font-weight: bold;
        color: #555;
    }

</style>
